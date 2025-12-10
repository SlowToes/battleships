package com.example.battleshipsonline.exception;

import org.springframework.http.HttpStatus;

public class InvalidPlayerException extends GameException {
    public InvalidPlayerException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
