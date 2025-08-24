package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ru.practicum.shareit.exception.CoincidenceException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("itemInMemory")
public class ItemInMemoryRepository implements ItemRepository {
    private UserRepository userRepository;
    private final Map<Long, Item> items = new HashMap<>();
    private long itemsCount = 0;

    @Autowired
    public ItemInMemoryRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return UserMapper.toUser(
                userRepository.getUserById(userId).orElseThrow(
                        () -> new NotFoundException(String.format("Пользователь с id - %d не найден.", userId)))
        );
    }

    @Override
    public ItemUpdate updateItem(long userId, long itemId, ItemUpdate itemUpdate) {
        Item item = ItemMapper.toItem(
                getItemById(itemId).orElseThrow(
                        () -> new NotFoundException(String.format("Предмет с id - %d не найден", itemId)))
        );

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
    public Optional<ItemDTO> getItemById(long itemId) {
        return Optional.ofNullable(ItemMapper.toItemDTO(items.get(itemId)));
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
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> {
                    String searchTextLC = searchText.toLowerCase(Locale.ROOT);
                    String nameLC = item.getName().toLowerCase(Locale.ROOT);
                    String descriptionLC = item.getDescription().toLowerCase(Locale.ROOT);

                    return nameLC.contains(searchTextLC) || descriptionLC.contains(searchTextLC);
                })
                .map(ItemMapper::toItemDTO)
                .collect(Collectors.toList());
    }
}
