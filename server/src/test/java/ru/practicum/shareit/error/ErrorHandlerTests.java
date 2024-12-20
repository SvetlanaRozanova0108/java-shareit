package ru.practicum.shareit.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ErrorHandlerTests {
    private final ObjectMapper objectMapper;
    private final BookingController bookingController;
    private final UserController userController;
    private final ObjectMapper mapper;

    private final String headerUserId = "X-Sharer-User-Id";


    @Test
    void handleNotFoundExceptionTest() throws Exception {
        BookingItemDto bookingItemDto = new BookingItemDto();
        bookingItemDto.setItemId(555L);
        bookingItemDto.setStart(LocalDateTime.now());
        bookingItemDto.setEnd(LocalDateTime.now().plusMinutes(1));
        String bookingSaveDtoJson = objectMapper.writeValueAsString(bookingItemDto);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        mockMvc.perform(post("/bookings")
                        .header(headerUserId, String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingSaveDtoJson))
                .andExpect(status().isNotFound())
                .andExpect(content().json(String.format("{\"error\":\"Вещь с Id 555 не найдена.\"}")));
    }

/*    @Test
    void handleValidationException() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        mockMvc.perform(post("/users")
                        .header(headerUserId, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"userName\"}"))
                .andExpect(status().isBadRequest());
    }*/
//
//    @Test
//    void handleForbiddenException() throws Exception {
//        BookingSaveDto bookingSaveDto = new BookingSaveDto();
//        bookingSaveDto.setItemId(10);
//        bookingSaveDto.setStart(LocalDateTime.now());
//        bookingSaveDto.setEnd(LocalDateTime.now().plusMinutes(1));
//        String bookingSaveDtoJson = objectMapper.writeValueAsString(bookingSaveDto);
//
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
//                .setControllerAdvice(new ErrorHandler())
//                .build();
//
//        mockMvc.perform(post("/bookings")
//                        .header(RequestHttpHeaders.USER_ID, String.valueOf(999))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(bookingSaveDtoJson))
//                .andExpect(status().isForbidden())
//                .andExpect(content().json(String.format("{\"error\":\"Доступ запрещен\", \"message\": \"Пользователь с ID 999 не найден\"}")));
//    }
//
//    @Test
//    void handleMissingRequestHeaderExceptionTest() throws Exception {
//        BookingSaveDto bookingSaveDto = new BookingSaveDto();
//        bookingSaveDto.setItemId(10);
//        bookingSaveDto.setStart(LocalDateTime.now());
//        bookingSaveDto.setEnd(LocalDateTime.now().plusMinutes(1));
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
//                .setControllerAdvice(new ErrorHandler())
//                .build();
//
//        mockMvc.perform(post("/bookings")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"itemId\": 10,\"start\":\"2024-12-11T22:27:47\",\"end\":\"2024-12-11T22:27:48\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(String.format("{\"error\":\"Не передан Header X-Sharer-User-Id\", \"message\": \"Required request header 'X-Sharer-User-Id' for method parameter type int is not present\"}")));
//
//    }
//
//    @Test
//    void notValidExceptionHandler() throws Exception {
//        BookingSaveDto bookingSaveDto = new BookingSaveDto();
//        bookingSaveDto.setItemId(10);
//        bookingSaveDto.setStart(LocalDateTime.now());
//        bookingSaveDto.setEnd(LocalDateTime.now().plusMinutes(1));
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
//                .setControllerAdvice(new ErrorHandler())
//                .build();
//
//        mockMvc.perform(post("/bookings")
//                        .header(RequestHttpHeaders.USER_ID, String.valueOf(10))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"itemId\": 50,\"start\":\"2024-12-11T22:27:47\",\"end\":\"2024-12-11T22:27:48\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(String.format("{\"error\":\"BadRequest\", \"message\": \"Item Не доступно для бронирования\"}")));
//    }
}