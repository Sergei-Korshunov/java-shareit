package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long id;

    @Future(message = "Дата начала бронирования должна быть в будущем")
    @NotNull(message = "Необходимо указать время начала бронирования")
    @JsonProperty(value = "start")
    private LocalDateTime startTime;

    @Future(message = "Дата окончания бронирования должна быть в будущем")
    @NotNull(message = "Необходимо указать время окончания бронирования")
    @JsonProperty(value = "end")
    private LocalDateTime endTime;

    @NotNull
    private ItemDTO item;

    @NotNull
    @JsonProperty(value = "booker")
    private UserDTO user;

    @NotNull(message = "Статус бронирования должен быть указан")
    private BookingStatus status;
}
