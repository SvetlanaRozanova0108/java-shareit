package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.time.LocalDateTime;

public class DateTimeStartBeforeEndValidator implements ConstraintValidator<DateTimeStartBeforeEnd, BookingItemDto> {

    @Override
    public void initialize(DateTimeStartBeforeEnd constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingItemDto bookingSaveDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingSaveDto.getStart();
        LocalDateTime end = bookingSaveDto.getEnd();
        return start != null && end != null && !start.isAfter(end);
    }
}
