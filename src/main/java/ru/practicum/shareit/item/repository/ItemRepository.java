package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    ItemDTO addItem(long userId, ItemDTO itemDTO);

    ItemUpdate updateItem(long userId, long itemId, ItemUpdate itemUpdate);

    Optional<ItemDTO> getItemById(long itemId);

    Collection<ItemDTO> getListItems(long userId);

    Collection<ItemDTO> search(String searchText);
}
