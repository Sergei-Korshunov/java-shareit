package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.Locale;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDTO createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody BookingRequestDTO bookingRequestDto) {
        BookingDTO createdBooking = bookingService.addRequestBooking(userId, bookingRequestDto);
        log.info("Бронирование создано: {}", createdBooking);

        return createdBooking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDTO approveBooking(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved) {
        BookingDTO approvedBooking = bookingService.bookingDecision(ownerId, bookingId, approved);
        log.info("Бронирование одобрено: {}, для ownerId - {}", approvedBooking, ownerId);

        return approvedBooking;
    }

    @GetMapping("/{bookingId}")
    public BookingDTO getBookingById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        log.info("Получение бронирования для пользователя по id - {}, id бронирования - {}", userId, bookingId);
        BookingDTO booking = bookingService.getDataBooking(userId, bookingId);
        log.info("Бронирование получено: {}", booking);

        return booking;
    }

    @GetMapping
    public List<BookingDTO> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение бронирований для пользователя по id - {}, состояние - {}", userId, state);
        List<BookingDTO> bookings =  bookingService.getListAllBookings(userId, BookingState.valueOf(state.toUpperCase(Locale.ROOT)));
        log.info("Бронирования пользователя получены: {}", bookings);

        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDTO> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Запрос бронирования для владельца по id - {}, состояние - {}", ownerId, state);
        List<BookingDTO> bookings = bookingService.getListAllBookingsOwner(ownerId, BookingState.valueOf(state.toUpperCase(Locale.ROOT)));
        log.info("Бронирования владельца получены: {}", bookings);

        return bookings;
    }
}
