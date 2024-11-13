package ru.practicum.shareit.exeption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyExistsException extends IllegalArgumentException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}