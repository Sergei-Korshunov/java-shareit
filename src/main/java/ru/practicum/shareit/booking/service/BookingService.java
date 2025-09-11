package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingRequestDTO;

import java.util.List;

public interface BookingService {
    BookingDTO addRequestBooking(Long userId, BookingRequestDTO bookingRequestDTO);

    BookingDTO bookingDecision(Long ownerId, Long bookingId, boolean approved);

    Booking getBookingById(Long bookingId);

    BookingDTO getDataBooking(Long userId, Long bookingId);

    List<BookingDTO> getListAllBookings(Long userId, BookingState bookingState);

    List<BookingDTO> getListAllBookingsOwner(Long ownerId, BookingState bookingState);
}