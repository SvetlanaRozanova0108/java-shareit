package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long requestId;
}
