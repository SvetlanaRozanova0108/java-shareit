package ru.practicum.shareit.exception;

import org.apache.coyote.BadRequestException;

public class DataTimeException extends BadRequestException {
    public DataTimeException(String message) {
        super(message);
    }
}
