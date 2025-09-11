package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class CommentMapper {

    public static Comment toComment(CommentDTO commentDTO, Item item, User author) {
        if (commentDTO == null) return new Comment();

        return new Comment(
                commentDTO.getId(),
                item,
                commentDTO.getTimeCreate(),
                author,
                commentDTO.getContent()
        );
    }

    public static CommentDTO toCommentDTO(Comment comment) {
        if (comment == null) return new CommentDTO();

        return new CommentDTO(
                comment.getId(),
                comment.getTimeCreate(),
                comment.getAuthor().getName(),
                comment.getContent()
        );
    }
}