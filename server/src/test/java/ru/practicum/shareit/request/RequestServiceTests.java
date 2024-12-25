package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestSaveDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTests {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    private final User user = new User(1L, "User", "user@email.com");
    private final UserDto userDto = new UserDto(1L, "User", "user@email.com");
    private final Request request = Request.builder()
            .id(1L)
            .requestor(user)
            .description("description")
            .build();
    private final RequestDto requestDto = RequestDto.builder()
            .id(1L)
            .description("description")
            .requestor(userDto)
            .items(new ArrayList<>())
            .build();
    private final RequestSaveDto requestSaveDto = RequestSaveDto.builder()
            .description("description")
            .build();

    @Test
    void getListYourRequestsTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        Mockito.when(requestRepository.findByRequestorIdOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(request));
        Mockito.when(itemRepository.findAllByRequest(any()))
                .thenReturn(new ArrayList<>());

        assertEquals(requestService.getListYourRequests(1L), List.of(requestDto));
    }

    @Test
    void getListAllRequestsTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        Mockito.when(itemRepository.findAllByRequest(any()))
                .thenReturn(new ArrayList<>());
        Mockito.when(requestRepository.findByRequestorIdIsNot(anyLong(), any()))
                .thenReturn(List.of(request));

        assertEquals(requestService.getListAllRequests(1L, 0, 10), List.of(requestDto));
    }

    @Test
    void getRequestByIdTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        Mockito.when(requestRepository.findById(any()))
                .thenReturn(Optional.ofNullable(request));
        Mockito.when(itemRepository.findAllByRequest(any()))
                .thenReturn(new ArrayList<>());

        assertEquals(requestService.getRequestById(1L, 1L), requestDto);
    }

    @Test
    void createRequestTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);

        Mockito.when(requestRepository.save(any()))
                .thenReturn(request);

        assertEquals(requestService.createRequest(1L, requestSaveDto), requestDto);
    }
}