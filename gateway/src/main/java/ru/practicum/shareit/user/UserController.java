package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDTO userDTO) {
        return userClient.addUser(userDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @RequestBody UserDTO userDTO,
            @Positive @PathVariable Long userId) {

        return userClient.updateUser(userDTO, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable Long userId) {
        return userClient.getUserById(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getListUsers() {
        return userClient.getListUsers();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> removeUser(@Positive @PathVariable Long userId) {
        return userClient.removeUser(userId);
    }

}
