package ru.practicum.shareit.user.mappers;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDTO;

public class UserMapper {
    public static User toUser(UserDTO userDTO) {
        if (userDTO == null) return null;

        return new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail());
    }

    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;

        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
