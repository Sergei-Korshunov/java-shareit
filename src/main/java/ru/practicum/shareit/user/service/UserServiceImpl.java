package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mappers.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        checkEmailUniqueness(userDTO.getEmail());
        User savedUser = userRepository.save(UserMapper.toUser(userDTO));

        return UserMapper.toUserDTO(savedUser);
    }

    private void checkEmailUniqueness(String email) {
        boolean isEmailExists = userRepository.existsByEmail(email);

        if (isEmailExists) {
            throw new EmailException(String.format("Email - '%s' уже существует.", email));
        }
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, long userId) {
        User user = getUserByIdWithOptional(userId);

        if (userDTO.getName() != null && !userDTO.getName().isBlank()) {
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
    public UserDTO getUserById(long userId) {
        return UserMapper.toUserDTO(getUserByIdWithOptional(userId));
    }

    protected User getUserByIdWithOptional(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id - %d не найден.", userId)));
    }

    @Override
    public Collection<UserDTO> getListUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void removeUser(long userId) {
        User user = getUserByIdWithOptional(userId);
        userRepository.delete(user);
    }
}
