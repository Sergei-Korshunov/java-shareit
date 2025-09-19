package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemResponseDTO;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDTOTest {

    @Autowired
    private JacksonTester<ItemRequestDTO> itemRequestDTOTester;
    private final ItemRequestDTO itemRequestDTO = new ItemRequestDTO(
            "Описание вещи"
    );

    @Autowired
    private JacksonTester<ItemResponseDTO> itemRequestDTOResponseTester;
    private final ItemResponseDTO itemRequestDTOResponse = new ItemResponseDTO(
            1L,
            "Описание вещи",
            null,
            LocalDateTime.of(2025, 10, 16, 12, 12),
            null
    );

    @Test
    void itemRequestDTO() throws Exception {
        JsonContent<ItemRequestDTO> jsonContent = itemRequestDTOTester.write(itemRequestDTO);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
    }

    @Test
    void itemRequestDTOResponse() throws Exception {
        JsonContent<ItemResponseDTO> jsonContent = itemRequestDTOResponseTester.write(itemRequestDTOResponse);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo("2025-10-16T12:12:00");
    }
}