package com.example.battleshipsonline.exception.ex;

import com.example.battleshipsonline.exception.GameException;
import org.springframework.http.HttpStatus;

public class InvalidCoordinateException extends GameException {
    public InvalidCoordinateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
