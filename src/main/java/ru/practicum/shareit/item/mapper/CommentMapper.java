package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),

                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getCreated());
    }

    public static List<CommentDto> toListDto(List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public static List<Comment> toList(List<CommentDto> comments) {
        if (comments == null) {
            return null;
        }
        return comments.stream()
                .map(CommentMapper::toComment)
                .collect(Collectors.toList());
    }
}
