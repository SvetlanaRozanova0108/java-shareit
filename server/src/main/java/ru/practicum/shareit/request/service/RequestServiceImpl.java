package ru.practicum.shareit.request.service;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestSaveDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public List<RequestDto> getListYourRequests(Long userId) {
        userService.getUserById(userId);
        List<RequestDto> listRequestDto = new ArrayList<>();
        List<Request> listRequest = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        listRequest.forEach(i -> {
            i.setItems(itemRepository.findAllByRequest(i));
            RequestDto requestDto = RequestMapper.toRequestDto(i);
            listRequestDto.add(requestDto);
        });
        return listRequestDto;
    }

    @Override
    public List<RequestDto> getListAllRequests(Long userId, Integer pageNum, Integer pageSize) {
        UserMapper.toUser(userService.getUserById(userId));
        List<RequestDto> listRequestDto = new ArrayList<>();
        Pageable page = PageRequest.of(pageNum, pageSize);
        List<Request> listRequest = requestRepository.findByRequestorIdIsNot(userId, page);
        listRequest.forEach(request -> {
            request.setItems(itemRepository.findAllByRequest(request));
            RequestDto requestDto = RequestMapper.toRequestDto(request);
            listRequestDto.add(requestDto);
        });
        return listRequestDto;
    }

    @Override
    public RequestDto getRequestById(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с ID " + requestId + " не найден.")));
        request.setItems(itemRepository.findAllByRequest(request));
        RequestDto requestDto = RequestMapper.toRequestDto(request);
        requestDto.setRequestor(userService.getUserById(userId));
        return requestDto;
    }

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, RequestSaveDto requestDto) {
        Request request = Request.builder()
                .description(requestDto.getDescription())
                .requestor(UserMapper.toUser(userService.getUserById(userId)))
                .created(LocalDateTime.now())
                .build();
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}
