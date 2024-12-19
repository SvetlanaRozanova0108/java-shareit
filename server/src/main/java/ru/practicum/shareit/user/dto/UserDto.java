package ru.practicum.shareit.user.dto;
import jakarta.validation.constraints.*;
import jdk.jshell.Snippet;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}
