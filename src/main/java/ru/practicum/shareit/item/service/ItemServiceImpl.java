package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingTimeDTO;
import ru.practicum.shareit.exception.CoincidenceException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDTO addItem(long userId, ItemDTO itemDTO) {
        User user = getUser(userId);

        itemDTO.setOwnerId(user.getId());
        Item savedItem = itemRepository.save(ItemMapper.toItem(itemDTO, user));

        List<Comment> comments = (List<Comment>) commentRepository.findByItemId(savedItem.getId());
        BookingTimeDTO lastBooking = getLastBooking(savedItem.getId());
        BookingTimeDTO nextBooking = getNextBooking(savedItem.getId());

        return ItemMapper.toItemDTO(savedItem, userId, comments, lastBooking, nextBooking);
    }

    private User getUser(long userId) {
        return UserMapper.toUser(userService.getUserById(userId));
    }

    private BookingTimeDTO getLastBooking(Long itemId) {
        return bookingRepository.findFirstByItemIdAndEndTimeBeforeOrderByEndTimeDesc(itemId, LocalDateTime.now())
                .map(booking -> new BookingTimeDTO(booking.getEndTime(), booking.getUser().getId()))
                .orElse(null);
    }

    private BookingTimeDTO getNextBooking(Long itemId) {
        return bookingRepository.findFirstByItemIdAndStartTimeAfterOrderByStartTimeAsc(itemId, LocalDateTime.now())
                .map(booking -> new BookingTimeDTO(booking.getStartTime(), booking.getUser().getId()))
                .orElse(null);
    }

    @Override
    public ItemUpdate updateItem(long userId, long itemId, ItemUpdate itemUpdate) {
        Item item = getItemByIdWithOptional(itemId);
        getUser(userId);

        if (item.getOwner().getId() != userId) {
            throw new CoincidenceException(
                    String.format("Идентификатор(%d) предмета не совпадает с владелцем по id - %d", itemId, userId));
        }

        String nameUpdate = itemUpdate.getName();
        String descriptionUpdate = itemUpdate.getDescription();
        Boolean available = itemUpdate.getAvailable();

        if (nameUpdate != null && !nameUpdate.isBlank()) {
            item.setName(nameUpdate);
        }

        if (descriptionUpdate != null && !descriptionUpdate.isBlank()) {
            item.setDescription(descriptionUpdate);
        }

        if (available != null) {
            item.setAvailable(itemUpdate.getAvailable());
        }

        Item updatedItem = itemRepository.save(item);

        List<Comment> comments = (List<Comment>) commentRepository.findByItemId(updatedItem.getId());
        BookingTimeDTO lastBooking = getLastBooking(updatedItem.getId());
        BookingTimeDTO nextBooking = getNextBooking(updatedItem.getId());

        return ItemMapper.toItemUpdate(item, comments, lastBooking, nextBooking);
    }

    @Override
    public ItemDTO getItemById(Long userId, long itemId) {
        Item item = getItemByIdWithOptional(itemId);

        Collection<Comment> comments = commentRepository.findByItemId(itemId);
        BookingTimeDTO lastBookingTime = getLastBooking(itemId);
        BookingTimeDTO nextBookingTime = getNextBooking(itemId);

        return ItemMapper.toItemDTO(item, userId, comments, lastBookingTime, nextBookingTime);
    }

    protected Item getItemByIdWithOptional(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с указанным id - %d не найден.", itemId)));
    }

    @Override
    public Collection<ItemDTO> getListItems(long userId) {
        List<Item> items = (List<Item>) itemRepository.findByOwnerId(userId);

        List<Long> itemIds = extractItemIds(items);
        Map<Long, List<Booking>> bookingsByItem = getBookingsMap(itemIds);
        Map<Long, List<Comment>> commentsByItem = getCommentsMap(itemIds);

        return items.stream()
                .map(item -> toItemDTOWithEnrichment(item, userId, bookingsByItem, commentsByItem))
                .collect(Collectors.toList());
    }

    private List<Long> extractItemIds(List<Item> items) {
        return items.stream().map(Item::getId).collect(Collectors.toList());
    }

    private Map<Long, List<Booking>> getBookingsMap(List<Long> itemIds) {
        return bookingRepository.findApprovedBookingsForItems(itemIds)
                .stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
    }

    private Map<Long, List<Comment>> getCommentsMap(List<Long> itemIds) {
        return commentRepository.findByItemIdIn(itemIds)
                .stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId()));
    }

    private ItemDTO toItemDTOWithEnrichment(Item item,
                                            Long userId,
                                            Map<Long, List<Booking>> bookingsMap,
                                            Map<Long, List<Comment>> commentsMap) {
        List<Booking> itemBookings = bookingsMap.getOrDefault(item.getId(), Collections.emptyList());

        return ItemMapper.toItemDTO(
                item,
                userId,
                commentsMap.getOrDefault(item.getId(), Collections.emptyList()),
                findLastBooking(itemBookings),
                findNextBooking(itemBookings));
    }

    private BookingTimeDTO findLastBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getEndTime().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEndTime))
                .map(booking -> new BookingTimeDTO(booking.getEndTime(), booking.getUser().getId()))
                .orElse(null);
    }

    private BookingTimeDTO findNextBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStartTime().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStartTime))
                .map(booking -> new BookingTimeDTO(booking.getStartTime(), booking.getUser().getId()))
                .orElse(null);
    }

    @Override
    public Collection<ItemDTO> search(String searchText) {
        if (searchText == null || searchText.isBlank())
            return Collections.emptyList();

        List<Item> items = itemRepository.searchAvailableItems(searchText);

        if (items.isEmpty()) return Collections.emptyList();

        Map<Long, List<Comment>> commentsByItem = getCommentsMap(extractItemIds(items));

        return items.stream()
                .map(item -> ItemMapper.toItemDTO(
                        item,
                        null,
                        commentsByItem.getOrDefault(item.getId(), Collections.emptyList()),
                        null,
                        null))
                .collect(Collectors.toList());
    }
}