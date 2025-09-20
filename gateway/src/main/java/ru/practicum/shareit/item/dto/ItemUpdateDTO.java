package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingTimeDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdateDTO {

    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long id;

    @NotBlank(message = "Имя не должен быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Статус аренды должен быть указан")
    private Boolean available;

    private BookingTimeDTO lastBookingTime;

    private BookingTimeDTO nextBookingTime;

    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long requestId;
}
