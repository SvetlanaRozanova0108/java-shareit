package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    List<BookingDto> getListAllBookingsUser(Long userId, String state, Integer from, Integer size);

    BookingDto getBookingInfo(Long userId, Long bookingId);

    List<BookingDto> getListBookingsAllItems(Long ownerId, String state);

    BookingDto createBooking(Long userId, BookingItemDto bookingItemDto);

    BookingDto responseBooking(Long bookingId, Long userId, Boolean approved);

}
