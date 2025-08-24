package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.Collections;

@Service
public class ItemService {
    private ItemRepository itemRepository;
    private UserService userService;

    @Autowired
    public ItemService(@Qualifier("itemInMemory") ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    public ItemDTO addItem(long userId, ItemDTO itemDTO) {
        userService.getUserById(userId);

        return itemRepository.addItem(userId, itemDTO);
    }

    public ItemUpdate updateItem(long userId, long itemId, ItemUpdate itemUpdate) {
        userService.getUserById(userId);

        return itemRepository.updateItem(userId, itemId, itemUpdate);
    }

    public ItemDTO getItemById(long itemId) {
        return itemRepository.getItemById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с указанным id - %d не найден.", itemId)));
    }

    public Collection<ItemDTO> getListItems(long userId) {
        userService.getUserById(userId);

        return itemRepository.getListItems(userId);
    }

    public Collection<ItemDTO> search(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(searchText);
    }
}
