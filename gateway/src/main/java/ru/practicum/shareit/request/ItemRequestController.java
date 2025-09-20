package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.request.dto.ItemRequestDTO;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addItemRequest(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemRequestDTO itemRequestDTO) {

        return itemRequestClient.addItemRequest(userId, itemRequestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getUserRequests(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getUserRequests(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Positive @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int pageSize) {

        return itemRequestClient.getAllRequests(userId, from, pageSize);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Positive @PathVariable Long requestId) {

        return itemRequestClient.getRequestById(userId, requestId);
    }
}