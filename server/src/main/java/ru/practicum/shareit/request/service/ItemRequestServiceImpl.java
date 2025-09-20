package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemResponseDTO;
import ru.practicum.shareit.request.mappers.ItemRequestMappers;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserService userService,
                                  ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemResponseDTO addItemRequest(Long userId, ItemRequestDTO itemRequestDTO) {
        User user = UserMapper.toUser(userService.getUserById(userId));
        ItemRequest itemRequest = ItemRequestMappers.toItemRequest(itemRequestDTO, user);

        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        return ItemRequestMappers.toItemRequestDTO(savedItemRequest, null);
    }

    @Override
    public List<ItemResponseDTO> getUserRequests(Long userId) {
        userService.getUserById(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByTimeCreateDesc(userId);

        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        Map<Long, List<Item>> idToItems = itemRepository.findByItemRequestIdIn(requestIds).stream()
                .collect(Collectors.groupingBy(item -> item.getItemRequest().getId()));

        return itemRequests.stream()
                .map(itemRequest ->
                        ItemRequestMappers.toItemRequestDTO(
                                itemRequest,
                                idToItems.getOrDefault(itemRequest.getId(), null)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDTO> getUsersAllRequests(Long userId, int from, int pageSize) {
        userService.getUserById(userId);

        Pageable page = PageRequest.of(from / pageSize, pageSize);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdNotOrderByTimeCreateDesc(userId, page);

        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        Map<Long, List<Item>> idToItems = itemRepository.findByItemRequestIdIn(requestIds).stream()
                .collect(Collectors.groupingBy(item -> item.getItemRequest().getId()));

        return itemRequests.stream()
                .map(itemRequest ->
                        ItemRequestMappers.toItemRequestDTO(
                                itemRequest,
                                idToItems.getOrDefault(itemRequest.getId(), null)))
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDTO getRequestDataById(Long userId, Long requestId) {
        userService.getUserById(userId);
        ItemRequest itemRequest = getRequestById(requestId);
        List<Item> items = itemRepository.findByItemRequestId(requestId);

        return ItemRequestMappers.toItemRequestDTO(itemRequest, items);
    }

    @Override
    public ItemRequest getRequestById(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id - %d не найден", requestId)));
    }
}