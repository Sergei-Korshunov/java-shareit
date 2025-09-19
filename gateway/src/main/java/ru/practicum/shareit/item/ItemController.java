package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;

@RestController
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addItem(
            @Positive @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDTO itemDTO) {

        return itemClient.addItem(userId, itemDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Positive @PathVariable Long itemId,
            @RequestBody ItemUpdateDTO itemUpdateDTO) {

        return itemClient.updateItem(userId, itemId, itemUpdateDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Positive @PathVariable Long itemId) {

        return itemClient.getItemById(userId, itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getListItems(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemClient.getListItems(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        if (!StringUtils.hasText(text))
            return null;

        return itemClient.search(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Positive @PathVariable Long itemId,
            @RequestBody CommentDTO commentDto) {

        return itemClient.addComment(userId, itemId, commentDto);
    }
}
