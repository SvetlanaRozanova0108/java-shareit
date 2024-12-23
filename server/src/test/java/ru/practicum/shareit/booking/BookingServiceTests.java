package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.DataTimeException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class BookingServiceTests {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private UserService userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private final User user1 = new User(1L, "User1", "user1@email.com");
    private final User user2 = new User(2L, "User2", "user2@email.com");
    private final UserDto userDto = new UserDto(1L, "User", "user@email.com");
    private final BookingItemDto bookingItemDto = BookingItemDto.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1L))
            .itemId(1L)
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .requestId(1L)
            .build();
    private final Item item1 = Item.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user1)
            .build();
    private final Item item2 = Item.builder()
            .id(1L)
            .name("Item2")
            .description("Description2")
            .available(true)
            .owner(user1)
            .build();
    private final Booking booking = Booking.builder()
            .booker(user2)
            .id(1L)
            .status(BookingStatus.APPROVED)
            .item(item1).build();

    @Test
    void getBookingInfoIsEmptyTest() {

        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception e = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingInfo(1L, 1L));

        assertEquals(e.getMessage(), String.format("Бронирование с Id " + 1L + " не найдено."));
    }

    @Test
    void getBookingInfoNotFoundExceptionTest() {

        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        Exception e = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingInfo(1L, 11L));

        assertEquals(e.getMessage(), String.format("Бронирование с Id " + 11L + " не найдено."));
    }

    @Test
    void getListAllBookingsUserNotFoundExceptionTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        Exception e = assertThrows(NotFoundException.class,
                () -> bookingService.getListAllBookingsUser(1L, "UnknownState"));

        assertEquals(e.getMessage(), "Состояние UNKNOWNSTATE не найдено.");
    }

    @Test
    void getListBookingsAllItemsNotFoundExceptionTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        Exception e = assertThrows(NotFoundException.class,
                () -> bookingService.getListBookingsAllItems(1L, "UnknownState"));

        assertEquals(e.getMessage(), "Состояние UNKNOWNSTATE не найдено.");
    }

    @Test
    void getListAllBookingsUserStatusRejectedTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        bookingService.getListAllBookingsUser(1L, "REJECTED");

        verify(bookingRepository).findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), eq(BookingStatus.REJECTED));
    }

    @Test
    void getListBookingsAllItemsStatusRejectedTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        bookingService.getListBookingsAllItems(1L, "REJECTED");

        verify(bookingRepository).findAllByItemIdInAndStatusOrderByStartDesc(anyLong(), any(), eq(BookingStatus.REJECTED));
    }


    @Test
    void createBookingTimeDataExceptionTest() {

        BookingItemDto bookingItemDto = BookingItemDto.builder()
                .start(LocalDateTime.now().plusHours(1L))
                .end(LocalDateTime.now().minusHours(1L))
                .itemId(1L)
                .build();
        Exception e = assertThrows(DataTimeException.class,
                () -> bookingService.createBooking(1L, bookingItemDto));

        assertEquals(e.getMessage(), String.format("Неверное время бронирования. " + bookingItemDto.getStart() + " - " + bookingItemDto.getEnd(),
                bookingItemDto.getStart(), bookingItemDto.getEnd()));
    }

    @Test
    void createBookingValidationExceptionTest() {

        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);

        Mockito.when(itemRepository.findById(anyLong()))
               .thenReturn(Optional.of(item1));
        Exception e = assertThrows(ValidationException.class,
                () -> bookingService.createBooking(1L, bookingItemDto));

        assertEquals(e.getMessage(), "Владелец не может быть заказчиком вещи.");
    }

    @Test
    void createBookingNotAvailableExceptionTest() {

        item1.setAvailable(false);
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item1));

        Exception e = assertThrows(NotAvailableException.class,
                () -> bookingService.createBooking(2L, bookingItemDto));

        assertEquals(e.getMessage(), String.format("Вещь с Id " + 1L + " недоступна."));
    }

    @Test
    void createBookingAvailableTest() {

        item1.setAvailable(true);
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito.when(bookingRepository.save(any())).thenReturn(booking);

        bookingService.createBooking(2L, bookingItemDto);

        verify(bookingRepository).save(any());
    }

    @Test
    void responseBookingAlreadyExistsExceptionTest() {

        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Exception e = assertThrows(AlreadyExistsException.class,
                () -> bookingService.responseBooking(1L, 1L, true));

        assertEquals(e.getMessage(), "Вещь уже забронирована.");
    }

    @Test
    void responseBookingNotAvailableExceptionTest() {

        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Exception e = assertThrows(NotAvailableException.class,
                () -> bookingService.responseBooking(1L, 10L, true));

        assertEquals(e.getMessage(), "У пользователя с ID 10 нет доступа к бронированию.");
    }

    @Test
    void responseBookingStatusApproveTrueTest() {
        booking.setStatus(BookingStatus.WAITING);
        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        bookingService.responseBooking(1L, 1L, true);
        verify(bookingRepository).update(BookingStatus.APPROVED, 1L);
    }

    @Test
    void responseBookingStatusApproveFalseTest() {
        booking.setStatus(BookingStatus.WAITING);
        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        bookingService.responseBooking(1L, 1L, false);
        verify(bookingRepository).update(BookingStatus.REJECTED, 1L);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "ALL, 0, 2",
            "CURRENT, -1, 1",
            "PAST, -2, -1",
            "FUTURE, 1, 2",
            "WAITING, 0, 1",
    })

    void getListAllBookingsUserTest(String state, int toStart, int toEnd) {
        LocalDateTime start = LocalDateTime.now().plusDays(toStart);
        LocalDateTime end = LocalDateTime.now().plusDays(toEnd);
        User testUser = new User(1L, "User", "user@email.com");
        Item testItem = item2;
        Booking testBooking = booking;
        testBooking.setBooker(testUser);
        testBooking.setStart(start);
        testBooking.setEnd(end);

        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testItem));
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        Mockito.when(bookingRepository.findAllByBookerIdOrderByStartDesc(Mockito.anyLong()))
                .thenReturn(List.of(testBooking));
        Mockito.when(bookingRepository.findAllCurrentBookingsByUser(
                Mockito.anyLong(),any())).thenReturn(List.of(testBooking));
        Mockito.when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                any())).thenReturn(List.of(testBooking));
        Mockito.when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(Mockito.anyLong(),
                any())).thenReturn(List.of(testBooking));
        Mockito.when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(Mockito.anyLong(),
                any())).thenReturn(List.of(testBooking));
        List<BookingDto> bookings = bookingService.getListAllBookingsUser(testUser.getId(), state);

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.get(0).getId(), 1L);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "ALL, 0, 2",
            "CURRENT, -1, 1",
            "PAST, -2, -1",
            "FUTURE, 1, 2",
    })

    void getListBookingsAllItemsTest(String state, int toStart, int toEnd) {
        LocalDateTime start = LocalDateTime.now().plusDays(toStart);
        LocalDateTime end = LocalDateTime.now().plusDays(toEnd);
        User booker = new User(1L, "User1", "user1@email.com");;
        User itemOwner = new User(2L, "User2", "user2@email.com");;
        Item testItem = Item.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(booker)
                .build();
        Booking testBooking = Booking.builder()
                .booker(user1)
                .id(1L)
                .status(BookingStatus.APPROVED)
                .item(testItem).build();
        testBooking.setBooker(booker);
        testBooking.setStart(start);
        testBooking.setEnd(end);

        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testItem));
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        Mockito.when(bookingRepository.findAllByItemIdOrderByStartDesc(Mockito.anyLong()))
                .thenReturn(List.of(testBooking));
        Mockito.when(bookingRepository.findAllCurrentBookingsByItemOwner(
                Mockito.anyLong(),any())).thenReturn(List.of(testBooking));
        Mockito.when(bookingRepository.findAllByItemIdInAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                any())).thenReturn(List.of(testBooking));
        Mockito.when(bookingRepository.findAllByItemIdInAndStartAfterOrderByStartDesc(
                Mockito.anyLong(),any())).thenReturn(List.of(testBooking));
        Mockito.when(bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(Mockito.anyLong(),
                any(), any())).thenReturn(List.of(testBooking));

        List<BookingDto> bookings = bookingService.getListBookingsAllItems(itemOwner.getId(), state);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0).getId(), 1L);
    }
}