package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemResponseDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemResponseDTO addItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        ItemResponseDTO itemResponseDTO = itemRequestService.addItemRequest(userId, itemRequestDTO);
        return itemResponseDTO;
    }

    @GetMapping
    public List<ItemResponseDTO> getUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemResponseDTO> requests = itemRequestService.getUserRequests(userId);
        return requests;
    }

    @GetMapping("/all")
    public List<ItemResponseDTO> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemResponseDTO> requests = itemRequestService.getUsersAllRequests(userId);
        return requests;
    }

    @GetMapping("/{requestId}")
    public ItemResponseDTO getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        ItemResponseDTO request = itemRequestService.getRequestDataById(userId, requestId);
        return request;
    }
}
