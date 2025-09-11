package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;

import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;

@Component
public interface UserService {
    UserDTO addUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO, long userId);

    UserDTO getUserById(long userId);

    Collection<UserDTO> getListUsers();

    boolean removeUser(long userId);
}
