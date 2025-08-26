package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.exception.CoincidenceException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final UserService userService;
    private final Map<Long, Item> items = new HashMap<>();
    private long itemsCount = 0;

    @Autowired
    public ItemServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ItemDTO addItem(long userId, ItemDTO itemDTO) {
        User user = getUser(userId);

        itemDTO.setId(nextItemsId());
        itemDTO.setOwner(user);
        items.put(itemDTO.getId(), ItemMapper.toItem(itemDTO));

        return itemDTO;
    }

    private long nextItemsId() {
        return ++itemsCount;
    }

    private User getUser(long userId) {
        return UserMapper.toUser(userService.getUserById(userId));
    }

    @Override
    public ItemUpdate updateItem(long userId, long itemId, ItemUpdate itemUpdate) {
        Item item = ItemMapper.toItem(getItemByIdWithOptional(itemId));
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

        return ItemMapper.toItemUpdate(item);
    }

    @Override
    public ItemDTO getItemById(long itemId) {
        return getItemByIdWithOptional(itemId);
    }

    protected ItemDTO getItemByIdWithOptional(long itemId) {
        return Optional.ofNullable(ItemMapper.toItemDTO(items.get(itemId))).orElseThrow(
                () -> new NotFoundException(String.format("Предмет с указанным id - %d не найден.", itemId)));
    }

    @Override
    public Collection<ItemDTO> getListItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .sorted(Comparator.comparing(Item::getId))
                .map(ItemMapper::toItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDTO> search(String searchText) {
        if (searchText == null || searchText.isBlank())
            return Collections.emptyList();

        String searchTextLC = searchText.toLowerCase(Locale.ROOT);

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> {
                    String nameLC = item.getName().toLowerCase(Locale.ROOT);
                    String descriptionLC = item.getDescription().toLowerCase(Locale.ROOT);

                    return nameLC.contains(searchTextLC) || descriptionLC.contains(searchTextLC);
                })
                .map(ItemMapper::toItemDTO)
                .collect(Collectors.toList());
    }
}
