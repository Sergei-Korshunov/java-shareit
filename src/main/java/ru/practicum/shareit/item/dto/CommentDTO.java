package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    @PositiveOrZero(message = "ID не должен быть отрицательным числом")
    private Long id;

    @JsonProperty(value = "created")
    private LocalDateTime timeCreate;

    private String authorName;

    @JsonProperty(value = "text")
    private String content;
}