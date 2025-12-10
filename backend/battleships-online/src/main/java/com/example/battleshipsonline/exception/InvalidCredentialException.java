package com.example.battleshipsonline.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialException extends GameException {
    public InvalidCredentialException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
