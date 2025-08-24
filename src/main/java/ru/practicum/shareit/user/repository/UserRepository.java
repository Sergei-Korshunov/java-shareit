package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    UserDTO addUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO, long userId);

    Optional<UserDTO> getUserById(long userId);

    Collection<UserDTO> getListUsers();

    boolean removeUser(long userId);
}
