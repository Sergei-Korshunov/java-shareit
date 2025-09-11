package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.shareit.booking.dto.BookingTimeDTO;
import ru.practicum.shareit.item.comment.CommentDTO;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long id;

    @NotBlank(message = "Имя не должен быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Статус аренды должен быть указан")
    private Boolean available;

    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long ownerId;

    private List<CommentDTO> comments;

    @JsonProperty(value = "lastBooking")
    private BookingTimeDTO lastBookingTime;

    @JsonProperty(value = "nextBooking")
    private BookingTimeDTO nextBookingTime;

    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long requestId;
}
