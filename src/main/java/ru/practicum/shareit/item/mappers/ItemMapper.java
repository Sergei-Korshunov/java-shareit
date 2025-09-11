package ru.practicum.shareit.item.mappers;

import ru.practicum.shareit.booking.dto.BookingTimeDTO;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static Item toItem(ItemDTO itemDTO, User owner) {
        if (itemDTO == null) return new Item();

        return new Item(
                itemDTO.getId(),
                itemDTO.getName(),
                itemDTO.getDescription(),
                itemDTO.getAvailable(),
                owner,
                itemDTO.getRequestId()
        );
    }

    public static ItemDTO toItemDTO(Item item, Long userId, Collection<Comment> comments, BookingTimeDTO lastBookingTime, BookingTimeDTO nextBookingTime) {
        if (item == null) return new ItemDTO();

        boolean isOwner = item.getOwner() != null && item.getOwner().getId().equals(userId);

        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                comments.stream()
                        .map(CommentMapper::toCommentDTO)
                        .collect(Collectors.toList()),
                isOwner ? lastBookingTime : null,
                isOwner ? nextBookingTime : null,
                item.getRequestId()
        );
    }

    public static ItemUpdate toItemUpdate(Item item, List<Comment> comments, BookingTimeDTO lastBookingTime, BookingTimeDTO nextBookingTime) {
        if (item == null) return new ItemUpdate();

        return new ItemUpdate(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                comments,
                lastBookingTime,
                nextBookingTime
        );
    }
}
