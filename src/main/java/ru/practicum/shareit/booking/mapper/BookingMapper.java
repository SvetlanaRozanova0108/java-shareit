package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookingMapper {

    public static List<BookingDto> toBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus());
    }

//    public static Booking toBooking(BookingDto bookingDto, UserDto booker) {
//        return new Booking(
//                bookingDto.getId(),
//                bookingDto.getStart(),
//                bookingDto.getEnd(),
//                ItemMapper.toItem(bookingDto.getItem()),
//                UserMapper.toUser(bookingDto.getBooker()),
//                bookingDto.getStatus());
//    }

    public static BookingBookerDto toBookingBookerDto(Booking booking) {
        return new BookingBookerDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd());
    }
//    public static Booking toBooking(BookingBookerDto bookingBookerDto, UserDto booker) {
//        return new Booking(
//                bookingBookerDto.getId(),
//                bookingBookerDto.getStart(),
//                bookingBookerDto.getEnd(),
//                null,
//                UserMapper.toUser(booker),
//                null
//                );
//    }
}
