package ru.practicum.shareit.item.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ItemService itemService, UserService userService, BookingRepository bookingRepository) {
        this.commentRepository = commentRepository;
        this.itemService = itemService;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public CommentDTO addComment(Long itemId, Long userId, CommentDTO commentDTO) {
        ItemDTO itemDTO = itemService.getItemById(userId, itemId);
        UserDTO userDTO = userService.getUserById(userId);

        if (!bookingRepository.existsByUserIdAndItemIdAndEndTimeBefore(userId, itemId, LocalDateTime.now())) {
            throw new BookingException("Пользователь не брал эту вещь в аренду");
        }

        User user = UserMapper.toUser(userDTO);
        Item item = ItemMapper.toItem(itemDTO, user);
        Comment comment = CommentMapper.toComment(commentDTO, item, user);

        comment.setTimeCreate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.toCommentDTO(savedComment);
    }
}