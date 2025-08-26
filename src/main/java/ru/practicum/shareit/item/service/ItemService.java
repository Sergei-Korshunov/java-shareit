package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;

import java.util.Collection;

@Component
public interface ItemService {
    ItemDTO addItem(long userId, ItemDTO itemDTO);

    ItemUpdate updateItem(long userId, long itemId, ItemUpdate itemUpdate);

    ItemDTO getItemById(long itemId);

    Collection<ItemDTO> getListItems(long userId);

    Collection<ItemDTO> search(String searchText);
}
