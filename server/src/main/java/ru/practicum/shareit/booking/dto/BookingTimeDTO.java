package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingTimeDTO {
    @NotNull(message = "Время бронирования должно быть указано")
    private LocalDateTime time;

    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    @NotNull(message = "ID пользователя не должен быть пустым")
    private Long userId;
}
