package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        try {
            log.info("Получение всех пользователей.");
            return userClient.getAllUsers();
        } catch (Exception e) {
            log.error("Ошибка получения пользователей.");
            throw e;
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        try {
            log.info("Получение пользователя по Id.");
            return userClient.getUserById(userId);
        } catch (Exception e) {
            log.error("Ошибка получения пользователя по Id.");
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            log.info("Создание пользователя.");
            return userClient.createUser(userDto);
        } catch (Exception e) {
            log.error("Ошибка создания пользователя.");
            throw e;
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long userId) {
        try {
            log.info("Обновление пользователя.");
            return userClient.updateUser(userDto, userId);
        } catch (Exception e) {
            log.error("Ошибка обновления пользователя.");
            throw e;
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        try {
            log.info("Удаление пользователя.");
            userClient.deleteUser(userId);
            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Ошибка удаления пользователя.");
            throw e;
        }
    }
}

