package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import ru.practicum.shareit.user.dto.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDTOTest {

    @Autowired
    private JacksonTester<UserDTO> userDTORequestTester;
    private final UserDTO userDTORequest = new UserDTO(
            null,
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Autowired
    private JacksonTester<UserDTO> userDTOUpdateTester;
    private final UserDTO userDTOUpdate = new UserDTO(
            null,
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Autowired
    private JacksonTester<UserDTO> userDTOResponseTester;
    private final UserDTO userDTOResponse = new UserDTO(
            1L,
            "Пользователь №1",
            "mail@mail.ru"
    );

    @Test
    void userDTORequest() throws Exception {
        JsonContent<UserDTO> jsonContent = userDTORequestTester.write(userDTORequest);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
    }

    @Test
    void userDTOUpdate() throws Exception {
        JsonContent<UserDTO> jsonContent = userDTOUpdateTester.write(userDTOUpdate);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
    }

    @Test
    void userDTOResponse() throws Exception {
        JsonContent<UserDTO> jsonContent = userDTOResponseTester.write(userDTOResponse);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Пользователь №1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("mail@mail.ru");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }
}