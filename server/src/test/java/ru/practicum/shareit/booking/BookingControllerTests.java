package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTests {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final String headerUserId = "X-Sharer-User-Id";

    private final ItemDto itemDto = ItemDto.builder()
            .name("testItem")
            .description("testDescription")
            .available(true)
            .build();
    private final BookingItemDto bookingItemDto = BookingItemDto.builder()
            .start(LocalDateTime.of(2020, 01, 01, 10, 00, 00))
            .end(LocalDateTime.of(2020, 01, 02, 10, 00, 00))
            .itemId(1L).build();
    private final BookingDto bookingDto = BookingDto.builder()
            .start(LocalDateTime.of(2020, 01, 01, 10, 00, 00))
            .end(LocalDateTime.of(2020, 01, 02, 10, 00, 00))
            .item(itemDto)
            .build();

    @Test
    void getListAllBookingsUserTest() throws Exception {
        Mockito.when(bookingService.getListAllBookingsUser(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        String result = mvc.perform(get("/bookings?state=ALL")
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, mapper.writeValueAsString(List.of(bookingDto)));
    }

    @Test
    void getListAllBookingsUserNotAvailableExceptionTest() throws Exception {

        doThrow(new NotAvailableException("")).when(bookingService).getListAllBookingsUser(anyLong(), anyString(), anyInt(), anyInt());

        mvc.perform(get("/bookings?state=ALL")
                        .header(headerUserId, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingInfoTest() throws Exception {
        Mockito.when(bookingService.getBookingInfo(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingItemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).getBookingInfo(1L, 1L);
    }

    @Test
    void getBookingInfoNotAvailableExceptionTest() throws Exception {
        doThrow(new NotAvailableException("")).when(bookingService).getBookingInfo(anyLong(), anyLong());

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingItemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getListBookingsAllItemsTest() throws Exception {
        Mockito.when(bookingService.getListBookingsAllItems(anyLong(), any()))
                .thenReturn(List.of(bookingDto));

        String result = mvc.perform(get("/bookings/owner?state=ALL")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, mapper.writeValueAsString(List.of(bookingDto)));
    }

    @Test
    void getListBookingsAllItemsNotAvailableExceptionTest() throws Exception {

        doThrow(new NotAvailableException("")).when(bookingService).getListBookingsAllItems(anyLong(), any());

        mvc.perform(get("/bookings/owner?state=ALL")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookingTest() throws Exception {
        Mockito.when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingItemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).createBooking(1L, bookingItemDto);
    }

    @Test
    void responseBookingTest() throws Exception {
        Mockito.when(bookingService.responseBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void responseBookingAlreadyExistsExceptionTest() throws Exception {
        doThrow(new AlreadyExistsException("")).when(bookingService).responseBooking(anyLong(), anyLong(), anyBoolean());

        mvc.perform(patch("/bookings/1?approved=true")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void responseBookingNotAvailableExceptionTest() throws Exception {

        doThrow(new NotAvailableException("")).when(bookingService).responseBooking(anyLong(), anyLong(), anyBoolean());

        mvc.perform(patch("/bookings/1?approved=true")
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}