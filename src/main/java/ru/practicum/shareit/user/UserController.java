package ru.practicum.shareit.user;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDTO addUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO addUser = userService.addUser(userDTO);
        log.info("Пользователь добавлен с id - {}", addUser.getId());

        return addUser;
    }

    @PatchMapping("/{userId}")
    public UserDTO updateUser(
            @RequestBody UserDTO userDTO,
            @PathVariable Long userId) {
        UserDTO updateUser = userService.updateUser(userDTO, userId);
        log.info("Обнавлен пользователь под id - {} с данными -> {}", userId, userDTO);

        return updateUser;
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Long userId) {
        UserDTO getUserById = userService.getUserById(userId);
        log.info("Получен пользователь с id - {} и данными -> {}", userId, getUserById);

        return getUserById;
    }

    @GetMapping
    public Collection<UserDTO> getListUsers() {
        Collection<UserDTO> getListUsers = userService.getListUsers();
        log.info("Получен список пользователей: {}", getListUsers);

        return getListUsers;
    }

    @DeleteMapping("/{userId}")
    public boolean removeUser(@PathVariable Long userId) {
        boolean isRemovedUser = userService.removeUser(userId);
        log.info("Удален пользователь с id - {}", userId);

        return isRemovedUser;
    }
}