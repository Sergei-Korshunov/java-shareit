package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {
    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long userId;

    @JsonProperty(value = "created")
    @NotNull
    private LocalDateTime timeCreate;

    @JsonProperty(value = "items")
    private List<ItemResponseOnRequestDTO> itemResponse;
}