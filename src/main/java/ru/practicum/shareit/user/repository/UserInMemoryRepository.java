package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;

import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mappers.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Repository("userInMemoryRepository")
public class UserInMemoryRepository implements UserRepository {
    private Map<Long, User> users = new HashMap<>();
    private long countUser = 0;

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        checkEmailUniqueness(userDTO.getEmail());
        userDTO.setId(nextUsersId());
        users.put(userDTO.getId(), UserMapper.toUser(userDTO));

        return userDTO;
    }

    private void checkEmailUniqueness(String email) {
        boolean isEmailExists = users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));

        if (isEmailExists) {
            throw new EmailException(String.format("Email - '%s' уже существует.", email));
        }
    }

    private long nextUsersId() {
        return ++countUser;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, long userId) {
        User user = UserMapper.toUser(getUserById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id - %d не найден.", userId))));

        if (userDTO.getName() != null && !userDTO.getName().isBlank()){
            user.setName(userDTO.getName());
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().isBlank()) {
            if (!user.getEmail().equals(userDTO.getEmail())) {
                checkEmailUniqueness(userDTO.getEmail());

                user.setEmail(userDTO.getEmail());
            }
        }

        return UserMapper.toUserDTO(user);
    }

    @Override
    public Optional<UserDTO> getUserById(long userId) {
        return Optional.ofNullable(UserMapper.toUserDTO(users.get(userId)));
    }

    @Override
    public Collection<UserDTO> getListUsers() {
        return users.values().stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean removeUser(long userId) {
        return users.remove(userId) != null;
    }
}
