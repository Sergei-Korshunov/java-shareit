package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public BookingDTO addRequestBooking(Long userId, BookingRequestDTO bookingRequestDTO) {
        UserDTO userDTO = userService.getUserById(userId);
        ItemDTO itemDTO = itemService.getItemById(userId, bookingRequestDTO.getItemId());

        if (!itemDTO.getAvailable())
            throw new BookingException(String.format("Предмет '%s' не доступен для бронирования.", itemDTO.getName()));

        User user = UserMapper.toUser(userDTO);
        Item item = ItemMapper.toItem(itemDTO, user);
        Booking booking = BookingMapper.toBooking(
                bookingRequestDTO,
                user,
                item
        );
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);

        return BookingMapper.toBookingDTO(savedBooking, itemDTO, userDTO);
    }

    @Override
    public BookingDTO bookingDecision(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = getBookingById(bookingId);

        if (!booking.getStatus().equals(BookingStatus.WAITING))
            throw new BookingException("Бронирование не встатусе ожидания.");

        if (!booking.getItem().getOwner().getId().equals(ownerId))
            throw new BookingException("Подвердить бронирование может только владелец вещи.");

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        Booking updatedBooking = bookingRepository.save(booking);

        ItemDTO itemDTO = itemService.getItemById(ownerId, updatedBooking.getItem().getId());
        UserDTO userDTO = userService.getUserById(updatedBooking.getUser().getId());

        return BookingMapper.toBookingDTO(updatedBooking, itemDTO, userDTO);
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Бронирование с id - %d не найдено.", bookingId)));
    }

    @Override
    public BookingDTO getDataBooking(Long userId, Long bookingId) {
        Booking booking = getBookingById(bookingId);

        if (!booking.getUser().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId))
            throw new BookingException("У Вас нет прав на просмотр данного бронирования.");

        ItemDTO itemDTO = itemService.getItemById(userId, booking.getItem().getId());
        UserDTO userDTO = userService.getUserById(booking.getUser().getId());

        return BookingMapper.toBookingDTO(booking, itemDTO, userDTO);
    }

    @Override
    public List<BookingDTO> getListAllBookings(Long userId, BookingState bookingState) {
        UserDTO userDTO = userService.getUserById(userId);

        return getListBookingsAllUsersThroughRequest(userId, bookingState)
                .stream()
                .map(booking -> {
                    ItemDTO itemDTO = ItemMapper.toItemDTO(booking.getItem(), userId, Collections.emptyList(), null, null);
                    return BookingMapper.toBookingDTO(booking, itemDTO, userDTO);
                })
                .collect(Collectors.toList());
    }

    private Collection<Booking> getListBookingsAllUsersThroughRequest(Long userId, BookingState bookingState) {
        LocalDateTime now = LocalDateTime.now();

        switch (bookingState) {
            case CURRENT -> {
                return bookingRepository.findByUserIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(userId, now, now);
            }
            case PAST -> {
                return bookingRepository.findByUserIdAndEndTimeBeforeOrderByStartTimeDesc(userId, now);
            }
            case FUTURE -> {
                return bookingRepository.findByUserIdAndStartTimeAfterOrderByStartTimeDesc(userId, now);
            }
            case WAITING -> {
                return bookingRepository.findByUserIdAndStatusOrderByStartTimeDesc(userId, BookingStatus.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findByUserIdAndStatusOrderByStartTimeDesc(userId, BookingStatus.REJECTED);
            }
            default -> {
                return bookingRepository.findByUserIdOrderByStartTimeDesc(userId);
            }
        }
    }

    @Override
    public List<BookingDTO> getListAllBookingsOwner(Long ownerId, BookingState bookingState) {
        UserDTO userDTO = userService.getUserById(ownerId);

        return getListAllBookingsOwnerThroughRequest(ownerId, bookingState)
                .stream()
                .map(booking -> {
                    ItemDTO itemDTO = ItemMapper.toItemDTO(booking.getItem(), ownerId, Collections.emptyList(), null, null);
                    return BookingMapper.toBookingDTO(booking, itemDTO, userDTO);
                })
                .collect(Collectors.toList());
    }

    private Collection<Booking> getListAllBookingsOwnerThroughRequest(Long ownerId, BookingState bookingState) {
        LocalDateTime now = LocalDateTime.now();

        switch (bookingState) {
            case CURRENT -> {
                return bookingRepository.findByItemOwnerIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(ownerId, now, now);
            }
            case PAST -> {
                return bookingRepository.findByItemOwnerIdAndEndTimeBeforeOrderByStartTimeDesc(ownerId, now);
            }
            case FUTURE -> {
                return bookingRepository.findByItemOwnerIdAndStartTimeAfterOrderByStartTimeDesc(ownerId, now);
            }
            case WAITING -> {
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartTimeDesc(ownerId, BookingStatus.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartTimeDesc(ownerId, BookingStatus.REJECTED);
            }
            default -> {
                return bookingRepository.findByItemOwnerIdOrderByStartTimeDesc(ownerId);
            }
        }
    }
}
