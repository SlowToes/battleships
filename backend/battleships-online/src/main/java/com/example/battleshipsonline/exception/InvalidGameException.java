package com.example.battleshipsonline.exception;

import org.springframework.http.HttpStatus;

public class InvalidGameException extends GameException {
    public InvalidGameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
