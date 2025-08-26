package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDTO {
    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull
    private User requestor;

    @NotNull
    private LocalDate created;
}
