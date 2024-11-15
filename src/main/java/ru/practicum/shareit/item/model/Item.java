package ru.practicum.shareit.item.model;

import lombok.*;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class Item {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long ownerId;

    private Long requestId;
}
