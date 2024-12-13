package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        try {
            log.info("Получение всех пользователей.");
            return userService.getAllUsers();
        } catch (Exception e) {
            log.error("Ошибка получения пользователей.");
            throw e;
        }
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        try {
            log.info("Получение пользователя по Id.");
            return userService.getUserById(userId);
        } catch (Exception e) {
            log.error("Ошибка получения пользователя по Id.");
            throw e;
        }
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        try {
            log.info("Создание пользователя.");
            return userService.createUser(userDto);
        } catch (Exception e) {
            log.error("Ошибка создания пользователя.");
            throw e;
        }
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable Long userId) {
        try {
            log.info("Обновление пользователя.");
            return userService.updateUser(userDto, userId);
        } catch (Exception e) {
            log.error("Ошибка обновления пользователя.");
            throw e;
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        try {
            log.info("Удаление пользователя.");
            userService.deleteUser(userId);
        } catch (Exception e) {
            log.error("Ошибка удаления пользователя.");
            throw e;
        }
    }
}

