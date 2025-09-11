package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDTO;

public interface CommentService {
    CommentDTO addComment(Long itemId, Long userId, CommentDTO commentDTO);
}