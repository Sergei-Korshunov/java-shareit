package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data.sql")
class ItemServiceTest {

    private final ItemService itemService;

    private final CommentService commentService;

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
            "Название вещи №5 новое",
            "Описание вещи №5 новое",
            false,
            null,
            null,
            null,
            null
    );

    private final CommentDTO commentDTO = new CommentDTO(
            null,
            null,
            null,
            "Комментарий"
    );

    @Test
    void addItem() {
        ItemDTO itemDtoResponse = itemService.addItem(1L, itemDTORequest);
        assertThat(itemDtoResponse.getName(), equalTo(itemDTORequest.getName()));
        assertThat(itemDtoResponse.getDescription(), equalTo(itemDTORequest.getDescription()));
        assertThat(itemDtoResponse.getAvailable(), equalTo(itemDTORequest.getAvailable()));
    }

    @Test
    void updateItem() {
        ItemUpdate itemUpdate = itemService.updateItem(5L, 5L, itemDTOUpdate);
        assertThat(itemUpdate.getName(), equalTo(itemDTOUpdate.getName()));
        assertThat(itemUpdate.getDescription(), equalTo(itemDTOUpdate.getDescription()));
        assertThat(itemUpdate.getAvailable(), equalTo(itemDTOUpdate.getAvailable()));

        assertThrows(NotFoundException.class, () -> itemService.updateItem(100L, 5L, itemDTOUpdate));
    }

    @Test
    void search() {
        List<ItemDTO> items = (List<ItemDTO>) itemService.search("Название вещи №4");
        assertThat(items.size(), equalTo(1));
        assertThat(items.getFirst().getName(), equalTo("Название вещи №4"));
    }

    @Test
    void addComment() {
        CommentDTO commentDtoResponse = commentService.addComment(1L, 2L, commentDTO);
        assertThat(commentDtoResponse.getContent(), equalTo(commentDTO.getContent()));

        assertThrows(BookingException.class, () -> commentService.addComment(2L, 1L, commentDTO));
    }
}