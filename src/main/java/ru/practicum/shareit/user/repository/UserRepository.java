package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    User getUserById(Long userId);

    User createUser(User user);

    User updateUser(User user);

    User deleteUser(Long userId);

    Long getUserByEmail(String input);

    boolean checkForUser(Long id);

    Boolean checkEmailExists(String input);
}
