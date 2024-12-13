package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                UserMapper.toUserDto(request.getRequestor()),
                request.getCreated(),
                request.getItems() != null ? request.getItems()
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>());
    }

    public static Request toRequest(RequestDto requestDto) {
        return new Request(
                requestDto.getId(),
                requestDto.getDescription(),
                UserMapper.toUser(requestDto.getRequestor()),
                requestDto.getCreated());
    }
}
