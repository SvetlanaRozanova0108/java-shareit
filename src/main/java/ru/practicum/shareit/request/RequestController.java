package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final String headerUserId = "X-Sharer-User-Id";
    private final RequestService requestService;

    @PostMapping
    public RequestDto createRequest(@RequestHeader(headerUserId) Long userId,
                                    @Valid @RequestBody RequestDto requestDto) {
        return requestService.createRequest(userId, requestDto);
    }
}
