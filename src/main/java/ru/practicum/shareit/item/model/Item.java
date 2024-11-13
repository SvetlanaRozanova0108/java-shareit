package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class Item {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long ownerId;
    private Long requestId;
}
