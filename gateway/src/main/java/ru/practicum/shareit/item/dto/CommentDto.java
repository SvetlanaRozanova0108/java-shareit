package ru.practicum.shareit.item.dto;

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
public class CommentDto {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 500)
    private String text;
    @NotBlank
    @Size(min = 1, max = 100)
    private String authorName;
    @PastOrPresent
    private LocalDateTime created;
}


