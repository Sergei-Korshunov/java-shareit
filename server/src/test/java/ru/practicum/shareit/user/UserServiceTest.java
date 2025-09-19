package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data.sql")
class UserServiceTest {

    private final UserService userService;
    UserDTO userDTORequest = new UserDTO(
            null,
            "Пользователь №6",
            "mail6@mail.ru"
    );

    @Test
    void addUser() {
        UserDTO user = userService.addUser(userDTORequest);
        UserDTO getUserById = userService.getUserById(user.getId());
        assertThat(user, equalTo(getUserById));
    }

    @Test
    void updateUser() {
        UserDTO userUpdate = new UserDTO(
                null,
                "Пользователь №1 новый",
                "mail1new@mail.ru"
        );

        UserDTO updateUser = userService.updateUser(userUpdate, 1L);
        assertThat(updateUser.getName(), equalTo("Пользователь №1 новый"));
        assertThat(updateUser.getEmail(), equalTo("mail1new@mail.ru"));
    }

    @Test
    void getUserById() {
        UserDTO getUserById = userService.getUserById(2L);
        assertThat(getUserById.getName(), equalTo("Пользователь №2"));
        assertThat(getUserById.getEmail(), equalTo("mail2@mail.ru"));
    }

    @Test
    void getListUser() {
        List<UserDTO> getListUser = (List<UserDTO>) userService.getListUsers();
        assertThat(getListUser.size(), equalTo(5));
        assertThat(getListUser.get(0).getName(), equalTo("Пользователь №1"));
        assertThat(getListUser.get(0).getEmail(), equalTo("mail1@mail.ru"));
    }

    @Test
    void removeUser() {
        userService.removeUser(5L);
        assertThrows(NotFoundException.class, () -> userService.getUserById(5L));
    }
}