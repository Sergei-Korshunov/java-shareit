package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDTOTest {

    @Autowired
    private JacksonTester<BookingRequestDTO> bookingRequestTester;
    private final BookingRequestDTO bookingRequestDTO = new BookingRequestDTO(
            null,
            LocalDateTime.of(2025, 10, 16, 12, 12),
            LocalDateTime.of(2025, 11, 16, 12, 12),
            1L,
            2L,
            null
    );

    @Autowired
    private JacksonTester<BookingDTO> bookingResponseTester;
    private final UserDTO userDTO = new UserDTO(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );

    private final ItemDTO itemDTOResponse = new ItemDTO(
            1L,
            "Название вещи",
            "Описание вещи",
            true,
            1L,
            null,
            null,
            null,
            2L
    );

    private final BookingDTO bookingDTOResponse = new BookingDTO(
            1L,
            LocalDateTime.of(2025, 10, 16, 12, 12),
            LocalDateTime.of(2025, 11, 16, 12, 12),
            itemDTOResponse,
            userDTO,
            BookingStatus.WAITING
    );

    @Test
    void bookingDTORequest() throws Exception {
        JsonContent<BookingRequestDTO> jsonContent = bookingRequestTester.write(bookingRequestDTO);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("2025-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("2025-11-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.booker").isEqualTo(2);
    }

    @Test
    void bookingDTOResponse() throws Exception {
        JsonContent<BookingDTO> jsonContent = bookingResponseTester.write(bookingDTOResponse);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo("2025-10-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo("2025-11-16T12:12:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.name").isEqualTo("Название вещи");
        assertThat(jsonContent).extractingJsonPathStringValue("$.item.description").isEqualTo("Описание вещи");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.email").isEqualTo("mail@mail.ru");
    }
}