package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.DataTimeException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingDto> getListAllBookingsUser(Long userId, String state) {
        userService.getUserById(userId);
        LocalDateTime time = LocalDateTime.now();
        log.info("userId: " + userId + ", state: " + state);
        switch (state) {
            case "ALL":
                return BookingMapper.toBookingDto(bookingRepository.findAllByBookerIdOrderByStartDesc(userId));
            case "CURRENT":
                return BookingMapper.toBookingDto(bookingRepository
                        .findAllCurrentBookingsByUser(userId, time));
            case "PAST":
                return BookingMapper.toBookingDto(bookingRepository
                        .findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, time));
            case "FUTURE":
                return BookingMapper.toBookingDto(bookingRepository
                        .findAllByBookerIdAndStartAfterOrderByStartDesc(userId, time));
            case "WAITING":
                return BookingMapper.toBookingDto(bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING));
            case "REJECTED":
                return BookingMapper.toBookingDto(bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED));
        }
        throw new NotFoundException(String.format("Состояние не найдено.", state));
    }

    @Override
    public BookingDto getBookingInfo(Long userId, Long bookingId) {
        userService.getUserById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с Id " + bookingId + " не найдено.")));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new NotFoundException(String.format("Пользователь с Id " + userId + " не является владельцем вещи, бронирование не доступно."));
        }
    }

    @Override
    public List<BookingDto> getListBookingsAllItems(Long ownerId, String state) {
        userService.getUserById(ownerId);
        LocalDateTime time = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return BookingMapper.toBookingDto(bookingRepository.findAllByItemIdOrderByStartDesc(ownerId));
            case "CURRENT":
                return BookingMapper.toBookingDto(bookingRepository.findAllCurrentBookingsByItemOwner(ownerId, time));
            case "PAST":
                return BookingMapper.toBookingDto(bookingRepository.findAllByItemIdInAndEndBeforeOrderByStartDesc(ownerId, time));
            case "FUTURE":
                return BookingMapper.toBookingDto(bookingRepository.findAllByItemIdInAndStartAfterOrderByStartDesc(ownerId, time));
            case "WAITING":
                return BookingMapper.toBookingDto(bookingRepository
                        .findAllByItemIdInAndStatusOrderByStartDesc(ownerId, time, BookingStatus.WAITING));
            case "REJECTED":
                return BookingMapper.toBookingDto(bookingRepository
                        .findAllByItemIdInAndStatusOrderByStartDesc(ownerId, time, BookingStatus.REJECTED));
        }
        throw new NotFoundException(String.format("Состояние не найдено.", state));
    }

    @SneakyThrows
    @Override
    @Transactional
    public BookingDto createBooking(Long userId, BookingItemDto bookingItemDto) {
        if (bookingItemDto.getEnd().isBefore(bookingItemDto.getStart()) ||
                bookingItemDto.getEnd().equals(bookingItemDto.getStart())) {
            throw new DataTimeException(String
                    .format("Неверное время бронирования.",
                            bookingItemDto.getStart(), bookingItemDto.getEnd()));
        }
        if (bookingItemDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Неверное время бронирования.");
        }
        Item item = itemRepository.findById(bookingItemDto.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с Id " + bookingItemDto.getItemId() + " не найдена.")));
        if (item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Владелец не может быть заказчиком вещи.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с Id = " + userId + " не найден.")));

        if (item.getAvailable()) {
            Booking booking = Booking.builder()
                    .start(bookingItemDto.getStart())
                    .end(bookingItemDto.getEnd())
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.WAITING)
                    .build();
            return BookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new NotAvailableException(String.format("Вещь с Id " + item.getId() + " недоступна."));
        }
    }

    @Override
    @Transactional
    public BookingDto responseBooking(Long bookingId, Long userId, Boolean approve) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с Id " + bookingId + " не найдено.")));
        Long ownerId = booking.getItem().getOwner().getId();
        if (ownerId.equals(userId)
                && booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new AlreadyExistsException("Вещь уже забронирована.");
        }
        if (!ownerId.equals(userId)) {
            throw new NotAvailableException(String.format("У пользователя с ID " + userId + " нет доступа к бронированию."));
        }
        if (approve) {
            booking.setStatus(BookingStatus.APPROVED);
            bookingRepository.update(BookingStatus.APPROVED, bookingId);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.update(BookingStatus.REJECTED, bookingId);
        }
        return BookingMapper.toBookingDto(booking);
    }
}

