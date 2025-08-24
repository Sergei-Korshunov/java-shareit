package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userInMemoryRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO addUser(UserDTO userDTO) {
        return userRepository.addUser(userDTO);
    }

    public UserDTO updateUser(UserDTO userDTO, long userId) {
        getUserById(userId);

        return userRepository.updateUser(userDTO, userId);
    }

    public UserDTO getUserById(long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id - %d не найден.", userId)));
    }

    public Collection<UserDTO> getListUsers() {
        return userRepository.getListUsers();
    }

    public boolean removeUser(long userId) {
        getUserById(userId);

        return userRepository.removeUser(userId);
    }
}
