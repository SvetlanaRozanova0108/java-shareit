package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getListYourRequests(Long userId);

    List<RequestDto> getListAllRequests(Long userId);

    RequestDto getRequestById(Long userId, Long requestId);

    RequestDto createRequest(Long userId, RequestDto requestDto);
}
