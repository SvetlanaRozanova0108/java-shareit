package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long currentId = 0L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public User createUser(User user) {
        user.setId(++currentId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(Long userId) {
        return users.remove(userId);
    }

    @Override
    public boolean checkForUser(Long id) {
        return users.containsKey(id);
    }

    @Override
    public Long getUserByEmail(String input) {
        if (input == null) {
            return null;
        }
        for (User user : users.values()) {
            String email = user.getEmail();
            if (email.equals(input)) {
                return user.getId();
            }
        }
        return null;
    }
}

