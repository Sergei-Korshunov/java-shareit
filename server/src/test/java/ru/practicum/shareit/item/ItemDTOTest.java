package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import ru.practicum.shareit.item.dto.*;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDTOTest {

    @Autowired
    private JacksonTester<ItemDTO> addItemTester;
    private final ItemDTO itemDTO = new ItemDTO(
            null,
            "Название вещи",
            "Описание вещи",
            true,
            1L,
            null,
            null,
            null,
            null
    );

    @Autowired
    private JacksonTester<ItemUpdate> updateItemTester;
    private final ItemUpdate itemUpdate = new ItemUpdate(
            null,
            "Название вещи",
            "Описание вещи",
            true,
            null,
            null,
            null,
            null
    );

    @Test
    void addItemDTO() throws Exception {
        JsonContent<ItemDTO> jsonContent = addItemTester.write(itemDTO);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
    }

    @Test
    void updateItemDTO() throws Exception {
        JsonContent<ItemUpdate> jsonContent = updateItemTester.write(itemUpdate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
    }
}