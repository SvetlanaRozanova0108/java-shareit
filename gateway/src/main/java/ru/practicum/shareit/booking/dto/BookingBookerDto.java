package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingBookerDto {

    private Long id;
    @NotBlank
    @Size(min = 1, max = 100)
    private Long bookerId;
    @FutureOrPresent
    private LocalDateTime start;
    @PastOrPresent
    private LocalDateTime end;
}

