package ru.practicum.shareit.item;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDTO addItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDTO itemDTO) {
        ItemDTO addItem = itemService.addItem(userId, itemDTO);
        log.info("Добавлен предмет с id - {}", addItem.getId());

        return addItem;
    }

    @PatchMapping("/{itemId}")
    public ItemUpdate updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemUpdate itemUpdate) {
        ItemUpdate updateItem = itemService.updateItem(userId, itemId, itemUpdate);
        log.info("Обнавлен предмет под id - {} с данными -> {}", itemId, itemUpdate);

        return updateItem;
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItemById(@PathVariable long itemId) {
        ItemDTO getItemById = itemService.getItemById(itemId);
        log.info("Получен предмет с id - {} и данными -> {}", itemId, getItemById);

        return getItemById;
    }

    @GetMapping
    public Collection<ItemDTO> getListItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        Collection<ItemDTO> getListItems = itemService.getListItems(userId);
        log.info("Получен список предметов: {}", getListItems);

        return getListItems;
    }

    @GetMapping("/search")
    public Collection<ItemDTO> search(@RequestParam String text) {
        Collection<ItemDTO> search = itemService.search(text);
        log.info("Найдены предметы по поиску '{}', предметы -> {}", text, search);

        return search;
    }
}
