package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long id;

    private LocalDate bookingStart;

    private LocalDate bookingEnd;

    @NotNull
    private Item itemBooking;

    @NotNull
    private User userBooking;

    @NotNull
    private BookingStatus bookingStatus;
}
