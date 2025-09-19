package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private final UserDTO userDTOResponse = new UserDTO(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );

    private final UserDTO userDTORequest = new UserDTO(
            null,
            "Пользователь №1",
            "mail@mail.ru"
    );

    private final UserDTO userDTOUpdate = new UserDTO(
            null,
            "Пользователь №1 обновлен",
            "mail@mail.ru"
    );

    @Test
    void addUser() throws Exception {
        when(userService.addUser(userDTORequest))
                .thenReturn(userDTOResponse);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDTORequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(userDTOUpdate, 1L))
                .thenReturn(userDTOResponse);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDTOUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDTOResponse.getName())))
                .andExpect(jsonPath("$.email", is(userDTOResponse.getEmail())));
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(1L))
                .thenReturn(userDTOResponse);

        mvc.perform(get("/users/1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDTOResponse.getName())))
                .andExpect(jsonPath("$.email", is(userDTOResponse.getEmail())));
    }

    @Test
    void getListUsers() throws Exception {
        when(userService.getListUsers())
                .thenReturn(List.of(userDTOResponse));

        mvc.perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(userDTOResponse.getName())))
                .andExpect(jsonPath("$[0].email", is(userDTOResponse.getEmail())));

    }

    @Test
    void removeUser() throws Exception {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}