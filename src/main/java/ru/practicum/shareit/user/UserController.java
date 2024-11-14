package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ItemService itemService;

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
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        try {
            log.info("Обновление пользователя.");
            return userService.updateUser(userDto, userId);
        } catch (Exception e) {
            log.error("Ошибка обновления пользователя.");
            throw e;
        }
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable Long userId) {
        try {
            log.info("Удаление пользователя.");
            UserDto userDto = userService.deleteUser(userId);
            itemService.deleteOwner(userId);
            return userDto;
        } catch (Exception e) {
            log.error("Ошибка удаления пользователя.");
            throw e;
        }
    }
}

