package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingClient bookingClient;
    private final String headerUserId = "X-Sharer-User-Id";
    private final Set<String> stateTypes = new HashSet<>(Arrays.asList("ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED"));

    @GetMapping()
    public ResponseEntity<Object> getListAllBookingsUser(@RequestHeader(headerUserId) Long userId,
                                                         @RequestParam(defaultValue = "ALL") @NotBlank String state,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "10") @Positive Integer size) {

        try {
            validateState(state);
            log.info("Получение списка всех бронирований текущего пользователя.");
            return bookingClient.getListAllBookingsUser(userId, state,from, size);
        } catch (Exception e) {
            log.error("Ошибка получения списка всех бронирований текущего пользователя.");
            throw e;
        }
    }

    private void validateState(String state) {
        if (!stateTypes.contains(state.toUpperCase())) {
            throw new ValidationException("State имеет неизвестное значение.");
        }
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingInfo(@RequestHeader(headerUserId) Long userId,
                                     @PathVariable Long bookingId) {

        try {
            log.info("Получение данных о конкретном бронировании.");
            return bookingClient.getBookingInfo(userId, bookingId);
        } catch (Exception e) {
            log.error("Ошибка получения данных о конкретном бронировании.");
            throw e;
        }
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getListBookingsAllItems(@RequestHeader(headerUserId) Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") String state) {

        try {
            validateState(state);
            log.info("Получение списка бронирований для всех вещей текущего пользователя.");
            return bookingClient.getListBookingsAllItems(ownerId, state);
        } catch (Exception e) {
            log.error("Ошибка получения списка бронирований для всех вещей текущего пользователя.");
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(headerUserId) Long userId,
                                    @Valid @RequestBody BookingItemDto bookingItemDto) {

        try {
            log.info("Добавление нового запроса на бронирование.");
            return bookingClient.createBooking(userId, bookingItemDto);

        } catch (Exception e) {
            log.error("Ошибка добавления нового запроса на бронирование.");
            throw e;
        }
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> responseBooking(@RequestHeader(headerUserId) Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam @NotBlank Boolean approved) {

        try {
            log.info("Подтверждение или отклонение запроса на бронирование.");
            return bookingClient.responseBooking(bookingId, userId, approved);

        } catch (NotFoundException e) {
            throw new NotAvailableException(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка подтверждения или отклонения запроса на бронирование.");
            throw e;
        }
    }
}

