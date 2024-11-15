package ru.practicum.shareit.user.model;
import lombok.*;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class User {

    private Long id;

    private String name;

    private String email;
}
