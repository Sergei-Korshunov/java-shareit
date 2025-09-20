package ru.practicum.shareit.request;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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

        log.info("Получение запроса на вещь: {}", itemRequestDTO);

        return itemResponseDTO;
    }

    @GetMapping
    public List<ItemResponseDTO> getUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemResponseDTO> getUserRequestsList = itemRequestService.getUserRequests(userId);

        log.info("Получение списка своих запросов: {}", getUserRequestsList);

        return getUserRequestsList;
    }

    @GetMapping("/all")
    public List<ItemResponseDTO> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<ItemResponseDTO> getAllUsersItemRequestsList = itemRequestService.getUsersAllRequests(userId, from, pageSize);

        log.info("Получение списка запросов других пользователей: {}", getAllUsersItemRequestsList);

        return getAllUsersItemRequestsList;
    }

    @GetMapping("/{requestId}")
    public ItemResponseDTO getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        ItemResponseDTO getRequestById = itemRequestService.getRequestDataById(userId, requestId);

        log.info("Получение инфорцации о запросе с id - {} --> {}", requestId, getRequestById);

        return getRequestById;
    }
}