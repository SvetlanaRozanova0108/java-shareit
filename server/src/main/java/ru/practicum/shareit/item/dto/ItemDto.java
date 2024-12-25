package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
    private BookingBookerDto nextBooking;
    private BookingBookerDto lastBooking;
    private List<CommentDto> comments;

    public ItemDto(Long id, String name, String description, Boolean available) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setAvailable(available);
    }
}

