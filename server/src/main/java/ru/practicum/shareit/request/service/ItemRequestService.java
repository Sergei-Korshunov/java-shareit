package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemResponseDTO;

import java.util.List;

public interface ItemRequestService {
    ItemResponseDTO addItemRequest(Long userId, ItemRequestDTO itemRequestDTO);

    List<ItemResponseDTO> getUserRequests(Long userId);

    List<ItemResponseDTO> getUsersAllRequests(Long userId);

    ItemResponseDTO getRequestDataById(Long userId, Long requestId);

    ItemRequest getRequestById(Long requestId);
}