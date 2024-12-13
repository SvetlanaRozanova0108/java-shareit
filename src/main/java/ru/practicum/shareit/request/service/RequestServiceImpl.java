package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, RequestDto requestDto) {
        Request request = Request.builder()
                .description(requestDto.getDescription())
                .requestor(UserMapper.toUser(userService.getUserById(userId)))
                .created(LocalDateTime.now())
                .build();
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}
