package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final String headerUserId = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getListYourRequests(@RequestHeader(headerUserId) Long userId) {
        try {
            log.info("Получение списка своих запросов.");
            return requestClient.getListYourRequests(userId);
        } catch (Exception e) {
            log.error("Ошибка получения списка своих запросов.");
            throw e;
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getListAllRequests(@RequestHeader(headerUserId) Long userId) {
        try {
            log.info("Получение списка всех запросов.");
            return requestClient.getListAllRequests(userId);
        } catch (Exception e) {
            log.error("Ошибка получения списка всех запросов.");
            throw e;
        }
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(headerUserId) Long userId,
                                     @PathVariable Long requestId) {
        try {
            log.info("Получение данных об одном конкретном запросе .");
            return requestClient.getRequestById(userId, requestId);
        } catch (Exception e) {
            log.error("Ошибка получения данных об одном конкретном запросе.");
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(headerUserId) Long userId,
                                    @Valid @RequestBody RequestDto requestDto) {
        try {
            log.info("Создание запроса.");
            return requestClient.createRequest(userId, requestDto);
        } catch (Exception e) {
            log.error("Ошибка создания запроса.");
            throw e;
        }
    }
}
