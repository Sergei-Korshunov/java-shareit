package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemResponseDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private final ItemRequestDTO itemRequestDTORequest = new ItemRequestDTO(
            "Описание запроса на вещь №1"
    );

    private final ItemResponseDTO itemRequestDTOResponse = new ItemResponseDTO(
            1L,
            "Описание запроса на вещь №1",
            null,
            LocalDateTime.now(),
            null
    );

    @Test
    void addItemRequest() throws Exception {
        when(itemRequestService.addItemRequest(1L, itemRequestDTORequest))
                .thenReturn(itemRequestDTOResponse);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDTORequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.description", is(itemRequestDTOResponse.getDescription())));
    }

    @Test
    void getUserRequests() throws Exception {
        when(itemRequestService.getUserRequests(1L))
                .thenReturn(List.of(itemRequestDTOResponse));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(itemRequestDTORequest.getDescription())));
    }

    @Test
    void getUsersAllRequests() throws Exception {
        when(itemRequestService.getUsersAllRequests(1L, 0, 10))
                .thenReturn(List.of(itemRequestDTOResponse));

        mvc.perform(get("/requests/all")
                .header("X-Sharer-User-Id", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestDataById() throws Exception {
        when(itemRequestService.getRequestDataById(1L, 1L))
                .thenReturn(itemRequestDTOResponse);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}