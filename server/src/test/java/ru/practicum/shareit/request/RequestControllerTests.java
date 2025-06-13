package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestSaveDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTests {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RequestService requestService;

    private final String headerUserId = "X-Sharer-User-Id";

    private final RequestDto requestDto = RequestDto
            .builder()
            .id(1L)
            .description("testDescription")
            .build();

    private final RequestSaveDto requestSaveDto = RequestSaveDto
            .builder()
            .description("testDescription")
            .build();

    @Test
    void getListYourRequestsTest() throws Exception {

        Mockito.when(requestService.getListYourRequests(anyLong()))
                .thenReturn(Collections.emptyList());

        String result = mvc.perform(get("/requests")
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, mapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    void getListYourRequestsValidationExceptionTest() throws Exception {

        doThrow(new ValidationException("")).when(requestService).getListYourRequests(anyLong());
        mvc.perform(get("/requests")
                        .header(headerUserId, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getListAllRequestsTest() throws Exception {

        Mockito.when(requestService.getListAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all?page=0&pagesize=10")
                        .header(headerUserId, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getListAllRequestsValidationExceptionTest() throws Exception {

        doThrow(new ValidationException("")).when(requestService).getListAllRequests(anyLong(), anyInt(), anyInt());

        mvc.perform(get("/requests/all?page=0&pagesize=10")
                        .header(headerUserId, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestByIdTest() throws Exception {

        Mockito.when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void getRequestByIdValidationExceptionTest() throws Exception {

        doThrow(new ValidationException("")).when(requestService).getRequestById(anyLong(), anyLong());

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header(headerUserId, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequestTest() throws Exception {

        Mockito.when(requestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestSaveDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(requestService).createRequest(1L, requestSaveDto);
    }

    @Test
    void createRequestValidationExceptionTest() throws Exception {

        doThrow(new ValidationException("")).when(requestService).createRequest(anyLong(), any());
        mvc.perform(post("/requests")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}