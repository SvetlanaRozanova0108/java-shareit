package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestSaveDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final String headerUserId = "X-Sharer-User-Id";
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getListYourRequests(@RequestHeader(headerUserId) Long userId) {
        try {
            log.info("Получение списка своих запросов.");
            return requestService.getListYourRequests(userId);
        } catch (Exception e) {
            log.error("Ошибка получения списка своих запросов.");
            throw e;
        }
    }

    @GetMapping("/all")
    public List<RequestDto> getListAllRequests(@RequestHeader(headerUserId) Long userId,
                                               @RequestParam(defaultValue = "0") String page,
                                               @RequestParam(defaultValue = "20") String pageSize) {
        try {
            log.info("Получение списка всех запросов.");
            return requestService.getListAllRequests(userId, Integer.parseInt(page), Integer.parseInt(pageSize));
        } catch (Exception e) {
            log.error("Ошибка получения списка всех запросов.");
            throw e;
        }
    }

    @GetMapping("{requestId}")
    public RequestDto getRequestById(@RequestHeader(headerUserId) Long userId,
                                     @PathVariable Long requestId) {
        try {
            log.info("Получение данных об одном конкретном запросе .");
            return requestService.getRequestById(userId, requestId);
        } catch (Exception e) {
            log.error("Ошибка получения данных об одном конкретном запросе.");
            throw e;
        }
    }

    @PostMapping
    public RequestDto createRequest(@RequestHeader(headerUserId) Long userId,
                                    @Valid @RequestBody RequestSaveDto requestDto) {
        try {
            log.info("Создание запроса.");
            return requestService.createRequest(userId, requestDto);
        } catch (Exception e) {
            log.error("Ошибка создания запроса.");
            throw e;
        }
    }
}
