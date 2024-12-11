package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final String headerUserId = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping()
    public List<BookingDto> getListAllBookingsUser(@RequestHeader(headerUserId) Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {

        try {
            log.info("Получение списка всех бронирований текущего пользователя.");
            return bookingService.getListAllBookingsUser(userId, state);
        } catch (Exception e) {
            log.error("Ошибка получения списка всех бронирований текущего пользователя.");
            throw e;
        }
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInfo(@RequestHeader(headerUserId) Long userId,
                                     @PathVariable Long bookingId) {

        try {
            log.info("Получение данных о конкретном бронировании.");
            return bookingService.getBookingInfo(userId, bookingId);
        } catch (Exception e) {
            log.error("Ошибка получения данных о конкретном бронировании.");
            throw e;
        }
    }

    @GetMapping("/owner")
    public List<BookingDto> getListBookingsAllItems(@RequestHeader(headerUserId) Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") String state) {

        try {
            log.info("Получение списка бронирований для всех вещей текущего пользователя.");
            return bookingService.getListBookingsAllItems(ownerId, state);
        } catch (Exception e) {
            log.error("Ошибка получения списка бронирований для всех вещей текущего пользователя.");
            throw e;
        }
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader(headerUserId) Long userId,
                                    @Valid @RequestBody BookingItemDto bookingItemDto) {

        try {
            log.info("Добавление нового запроса на бронирование.");
            return bookingService.createBooking(userId, bookingItemDto);

        } catch (Exception e) {
            log.error("Ошибка добавления нового запроса на бронирование.");
            throw e;
        }
    }

    @PatchMapping("/{bookingId}")
    public BookingDto responseBooking(@RequestHeader(headerUserId) Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam Boolean approved) {

        try {
            log.info("Подтверждение или отклонение запроса на бронирование.");
            return bookingService.responseBooking(bookingId, userId, approved);

        } catch (NotFoundException e) {
            throw new NotAvailableException(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка подтверждения или отклонения запроса на бронирование.");
            throw e;
        }
    }
}
