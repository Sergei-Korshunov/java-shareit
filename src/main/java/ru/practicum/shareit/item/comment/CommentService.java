package ru.practicum.shareit.item.comment;

public interface CommentService {
    CommentDTO addComment(Long itemId, Long userId, CommentDTO commentDTO);
}