package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTests {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;
    private final UserDto userDto = new UserDto(1L, "User1", "user1@email.com");

    @Test
    void getAllUsersTest() throws Exception {
        //Arrange
                Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(userDto));
        //Act
        String sut = mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //Assert
        assertEquals(sut, mapper.writeValueAsString(List.of(userDto)));
    }

    @Test
    void getAllUsersThrowBadRequestTest() throws Exception {

        doThrow(new ValidationException("")).when(userService).getAllUsers();

        mvc.perform(get("/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserByIdTest() throws Exception {
        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);

        String result = mvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, mapper.writeValueAsString(userDto));
    }

    @Test
    void getUserByIdThrowBadRequestTest() throws Exception {

        doThrow(new ValidationException("")).when(userService).getUserById(anyLong());

        mvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserTest() throws Exception {

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).createUser(userDto);
    }

    @Test
    void createUserThrowBadRequestTest() throws Exception {

        doThrow(new ValidationException("")).when(userService).createUser(any());

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(userService).createUser(userDto);
    }

    @Test
    void updateUserTest() throws Exception {
        var userId = 1L;
        UserDto updateUserDto = UserDto.builder()
                .id(userId)
                .name("updateUser")
                .email("updateuser@email.com")
                .build();

        mvc.perform(patch("/users/{id}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(updateUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).updateUser(updateUserDto, userId);
    }

    @Test
    void updateUserThrowBadRequestTest() throws Exception {
        var userId = 1L;
        UserDto updateUserDto = UserDto.builder()
                .id(userId)
                .name("updateUser")
                .email("updateuser@email.com")
                .build();
        doThrow(new ValidationException("")).when(userService).updateUser(updateUserDto, userId);


        mvc.perform(patch("/users/{id}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(updateUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService).updateUser(updateUserDto, userId);
    }

    @Test
    void deleteUserTest() throws Exception {
        var userId = 1L;

        mvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).deleteUser(userId);
    }

    @Test
    void deleteUserTestThrowNotFound() throws Exception {
        var userId = 100L;
        doThrow(new NotFoundException("")).when(userService).deleteUser(anyLong());

        mvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(userId);
    }
}