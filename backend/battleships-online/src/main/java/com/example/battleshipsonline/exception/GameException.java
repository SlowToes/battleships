package com.example.battleshipsonline.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public abstract class GameException extends RuntimeException {
    @Getter
    private final HttpStatus status;

    public GameException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
