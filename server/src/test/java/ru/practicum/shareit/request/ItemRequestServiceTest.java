package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemResponseDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data.sql")
class ItemRequestServiceTest {

    private final ItemRequestService itemRequestService;
    private final ItemRequestDTO itemRequestDTORequest = new ItemRequestDTO(
            "Новый запрос на поиск вещи"
    );

    @Test
    void addItemRequest() {
        ItemResponseDTO itemResponseDTO = itemRequestService.addItemRequest(1L, itemRequestDTORequest);
        assertThat(itemRequestDTORequest.getDescription(), equalTo(itemResponseDTO.getDescription()));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.addItemRequest(100L, itemRequestDTORequest));
    }

    @Test
    void getUserRequests() {
        List<ItemResponseDTO> listItemResponseDTO = itemRequestService.getUserRequests(5L);
        assertThat(listItemResponseDTO.size(), equalTo(1));
        assertThat(listItemResponseDTO.getFirst().getId(), equalTo(5L));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getUserRequests(100L));
    }

    @Test
    void getUsersAllRequests() {
        List<ItemResponseDTO> listItemResponseDTO = itemRequestService.getUsersAllRequests(4L);
        assertThat(listItemResponseDTO.size(), equalTo(1));
    }
}