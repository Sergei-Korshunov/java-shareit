package ru.practicum.shareit.item.mappers;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item toItem(ItemDTO itemDTO) {
        if (itemDTO == null) return null;

        return new Item(
                itemDTO.getId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                itemDTO.getOwner(),
                itemDTO.getItemRequest()
        );
    }

    public static ItemDTO toItemDTO(Item item) {
        if (item == null) return null;

        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getItemRequest()
        );
    }

    public static ItemUpdate toItemUpdate(Item item) {
        if (item == null) return null;

        return new ItemUpdate(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }
}
