package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    private final BookingRequestDTO bookingRequestDTO = new BookingRequestDTO(
            1L,
            LocalDateTime.now().plusHours(2),
            LocalDateTime.now().plusDays(1),
            2L,
            null,
            null
    );

    private final UserDTO userDTO = new UserDTO(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );

    private final ItemDTO itemDTOResponse = new ItemDTO(
            2L,
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
    void createBooking() throws Exception {
        when(bookingService.addRequestBooking(1L, bookingRequestDTO))
                .thenReturn(bookingDTOResponse);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingRequestDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.bookingDecision(1L, 1L, true))
                .thenReturn(bookingDTOResponse);

        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingRequestDTO))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDTOResponse.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDTOResponse.getStatus().toString())));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getDataBooking(1L, 1L))
                .thenReturn(bookingDTOResponse);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDTOResponse.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDTOResponse.getStatus().toString())));
    }

    @Test
    void getUserBookings() throws Exception {
        when(bookingService.getListAllBookings(1L, BookingState.ALL))
                .thenReturn(List.of(bookingDTOResponse));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDTOResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDTOResponse.getStatus().toString())));
    }

    @Test
    void getOwnerBookings() throws Exception {
        when(bookingService.getListAllBookingsOwner(1L, BookingState.ALL))
                .thenReturn(List.of(bookingDTOResponse));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDTOResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDTOResponse.getStatus().toString())));
    }
}