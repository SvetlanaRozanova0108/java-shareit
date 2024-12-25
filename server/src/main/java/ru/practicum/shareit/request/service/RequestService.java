package ru.practicum.shareit.request.service;

import jakarta.validation.constraints.PositiveOrZero;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestSaveDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getListYourRequests(Long userId);

    List<RequestDto> getListAllRequests(Long userId, @PositiveOrZero Integer page, @PositiveOrZero Integer pageSize);

    RequestDto getRequestById(Long userId, Long requestId);

    RequestDto createRequest(Long userId, RequestSaveDto requestDto);
}
