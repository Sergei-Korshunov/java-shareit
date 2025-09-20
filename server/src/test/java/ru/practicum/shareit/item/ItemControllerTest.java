package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    private final ItemDTO itemDTOResponse = new ItemDTO(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            2L,
            null,
            null,
            null,
            null
    );

    private final ItemDTO itemDTORequest = new ItemDTO(
            null,
            "Название вещи",
            "Описание вещи",
            true,
            2L,
            null,
            null,
            null,
            null
    );

    private final ItemUpdate itemDTOUpdate = new ItemUpdate(
            null,
            "Название вещи",
            "Описание вещи",
            false,
            null,
            null,
            null,
            null
    );

    private final CommentDTO commentDTORequest = new CommentDTO(
            null,
            null,
            null,
            "Текст комментария"
    );

    private final CommentDTO commentDTOResponse = new CommentDTO(
            1L,
            LocalDateTime.now(),
            "Автор комментария",
            "Текст комментария"
    );

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(1L, itemDTORequest))
                .thenReturn(itemDTOResponse);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDTORequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(1L, 1L, itemDTOUpdate))
                .thenReturn(itemDTOUpdate);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDTOUpdate))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDTOResponse.getName())))
                .andExpect(jsonPath("$.description", is(itemDTOResponse.getDescription())));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(1L, 1L))
                .thenReturn(itemDTOResponse);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDTOUpdate.getName())))
                .andExpect(jsonPath("$.description", is(itemDTOUpdate.getDescription())));
    }

    @Test
    void getListItems() throws Exception {
        when(itemService.getListItems(1L))
                .thenReturn(List.of(itemDTOResponse));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDTOUpdate.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDTOUpdate.getDescription())));

    }

    @Test
    void search() throws Exception {
        when(itemService.search("Название вещи"))
                .thenReturn(List.of(itemDTOResponse));
        mvc.perform(get("/items/search?text=Название вещи").characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDTOUpdate.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDTOUpdate.getDescription())));
    }

    @Test
    void addComment() throws Exception {
        when(commentService.addComment(1L, 1L, commentDTORequest))
                .thenReturn(commentDTOResponse);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDTORequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}