package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.AlreadyExistsException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с Id = " + id + " не найден.");
        }
        return userMapper.toUserDto(userRepository.getUserById(id));
    }

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email пользователя не может быть пустым.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Имя пользователя не может быть пустым.");
        }
        Long input = userRepository.getUserByEmail(user.getEmail());
        if (input != null) {
            throw new AlreadyExistsException("Пользователь с email " + user.getEmail() + " уже существует.");
        }
        return userMapper.toUserDto(userRepository.createUser(user));
    }

    public UserDto updateUser(UserDto userDto, Long id) {
        userDto.setId(id);
        if (userDto.getName() == null) {
            userDto.setName(userRepository.getUserById(id).getName());
        }
        if (userDto.getEmail() == null) {
            userDto.setEmail(userRepository.getUserById(id).getEmail());
        }
        User user = userMapper.toUser(userDto);
        if (userRepository.getUserById(user.getId()) == null) {
            throw new NotFoundException("Пользователь с Id = " + user.getId() + " не найден.");
        }
        if (user.getId() == null) {
            throw new ValidationException("Пользователь не может быть пустым.");
        }
        Long input = userRepository.getUserByEmail(user.getEmail());
        if (input != null && !user.getId().equals(input)) {
            throw new AlreadyExistsException("Пользователь с email = " + user.getEmail() + " уже существует.");
        }
        User updateUser = userRepository.updateUser(user);
        return userMapper.toUserDto(updateUser);
    }

    public UserDto deleteUser(Long userId) {
        if (userId == null) {
            throw new ValidationException("Пользователь не может быть пустым.");
        }
        if (!userRepository.checkForUser(userId)) {
            throw new NotFoundException("Пользователь с Id = " + userId + " не найден.");
        }
        return userMapper.toUserDto(userRepository.deleteUser(userId));
    }
}

