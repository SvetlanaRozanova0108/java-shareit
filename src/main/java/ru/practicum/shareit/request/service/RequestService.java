package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

public interface RequestService {
    //List<RequestDto> getAllRequests(Long userId);

    //RequestDto getRequestById(Long userId, Long requestId);

    RequestDto createRequest(Long userId, RequestDto requestDto);
}
