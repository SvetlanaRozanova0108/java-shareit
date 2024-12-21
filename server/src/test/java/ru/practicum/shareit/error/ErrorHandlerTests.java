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
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ErrorHandlerTests {
    private final ObjectMapper objectMapper;
    private final BookingController bookingController;

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
}