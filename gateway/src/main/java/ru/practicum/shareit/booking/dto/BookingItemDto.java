package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.DateTimeStartBeforeEnd;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DateTimeStartBeforeEnd
public class BookingItemDto {

    @NotNull
    private Long itemId;
    @NotNull
    @Future
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;

}
