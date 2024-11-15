package ru.practicum.shareit.user.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}
