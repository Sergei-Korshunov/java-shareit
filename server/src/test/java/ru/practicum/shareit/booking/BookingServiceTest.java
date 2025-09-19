package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.service.BookingService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data.sql")
class BookingServiceTest {

    private final BookingService bookingService;
    private final BookingRequestDTO bookingDTORequest = new BookingRequestDTO(
            1L,
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusDays(1),
            2L,
            null,
            null
    );

    @Test
    void createBooking() {
        BookingDTO bookingDtoResponse = bookingService.addRequestBooking(1L, bookingDTORequest);
        assertThat(bookingDtoResponse.getStartTime(), equalTo(bookingDTORequest.getStartTime()));
        assertThat(bookingDtoResponse.getEndTime(), equalTo(bookingDTORequest.getEndTime()));
    }

    @Test
    void bookingDecision() {
        BookingDTO bookingDtoResponse = bookingService.bookingDecision(2L, 2L, true);
        assertThat(bookingDtoResponse.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void getBookingById() {
        BookingDTO bookingDtoResponse = bookingService.getDataBooking(2L, 5L);
        assertThat(bookingDtoResponse.getStatus(), equalTo(BookingStatus.CANCELED));
    }

    @Test
    void getAllBookingCurrentUser() {
        List<BookingDTO> bookingDtoResponse = bookingService.getListAllBookings(2L, BookingState.ALL);
        assertThat(bookingDtoResponse.getFirst().getStatus(), equalTo(BookingStatus.CANCELED));
        assertThat(bookingDtoResponse.size(), equalTo(2));
    }
}