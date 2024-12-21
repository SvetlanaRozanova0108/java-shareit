package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;


    @Mock
    private UserRepository userRepository;
    private final User user1 = new User(1L, "User1", "user1@email.com");
    private final UserDto userDto1 = new UserDto(1L, "User1", "user1@email.com");
    private final User user2 = new User(2L, "User2", "user2@email.ru");
    private final UserDto userDto2 = new UserDto(2L, "User2", "user2@email.ru");

    @Test
    void getAllUsersTest() {

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserDto> users = userService.getAllUsers();

        assertEquals(users, List.of(userDto1, userDto2));
    }

    @Test
    void getUserByIdTest() {

        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));

        var sut = userService.getUserById(1L);

        assertEquals(sut, userDto1);
    }

    @Test
    void createUserTest() {

        Mockito.when(userRepository.save(any()))
                .thenReturn(user1);

        assertEquals(userService.createUser(userDto1), userDto1);
    }

    @Test
    void updateUserTest() {

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));

        User updateUser = new User(1L, "updateUser", "updateUser@email.ru");
        UserDto updateUserDto = new UserDto(1L, "updateUser", "updateUser@email.ru");
        Mockito.when(userRepository.save(any()))
                .thenReturn(updateUser);

        assertEquals(userService.updateUser(updateUserDto, 1L), updateUserDto);
    }

    @Test
    void updateUserNameTest() {

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));

        User updateUser = new User(1L, null, "updateUser@email.ru");
        UserDto updateUserDto = new UserDto(1L, null, "updateUser@email.ru");
        Mockito.when(userRepository.save(any()))
                .thenReturn(updateUser);

        assertEquals(userService.updateUser(updateUserDto, 1L), updateUserDto);
    }

    @Test
    void updateUserEmailTest() {

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));

        User updateUser = new User(1L, "updateUser", null);
        UserDto updateUserDto = new UserDto(1L, "updateUser", null);
        Mockito.when(userRepository.save(any()))
                .thenReturn(updateUser);

        assertEquals(userService.updateUser(updateUserDto, 1L), updateUserDto);
    }

    @Test
    void deleteUserTest() {

        userService.deleteUser(1L);

        Mockito.verify(userRepository).deleteById(1L);
    }

}
