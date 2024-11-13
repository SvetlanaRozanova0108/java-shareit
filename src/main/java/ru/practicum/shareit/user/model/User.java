package ru.practicum.shareit.user.model;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class User {

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Email
    private String email;
}
