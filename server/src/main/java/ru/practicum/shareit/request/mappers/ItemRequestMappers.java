package ru.practicum.shareit.request.mappers;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemResponseDTO;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDTO;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMappers {

    public static ItemRequest toItemRequest(ItemRequestDTO itemRequestDTO, User user) {
        if (itemRequestDTO == null || user == null)
            return new ItemRequest();

        return new ItemRequest(
                itemRequestDTO.getDescription(),
                user,
                LocalDateTime.now()
        );
    }

    public static ItemResponseDTO toItemRequestDTO(ItemRequest itemRequest, List<Item> items) {
        if (itemRequest == null)
            return new ItemResponseDTO();

        return new ItemResponseDTO(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester().getId(),
                itemRequest.getTimeCreate(),
                items == null ? Collections.emptyList() : items.stream()
                        .map(ItemRequestMappers::toItemResponseOnRequestDTO)
                        .collect(Collectors.toList())
        );
    }

    public static ItemResponseOnRequestDTO toItemResponseOnRequestDTO(Item item) {
        if (item == null)
            return new ItemResponseOnRequestDTO();

        return new ItemResponseOnRequestDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getItemRequest().getId()
        );
    }
}