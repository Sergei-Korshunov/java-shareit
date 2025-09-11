package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
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

    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    @NotNull(message = "ID вещи не должен быть пустым")
    private Long itemId;

    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    @JsonProperty(value = "booker")
    private Long requestUserId;

    private BookingStatus status;
}