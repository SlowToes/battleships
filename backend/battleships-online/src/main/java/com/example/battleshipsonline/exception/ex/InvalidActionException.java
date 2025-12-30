package com.example.battleshipsonline.exception.ex;

import com.example.battleshipsonline.exception.GameException;
import org.springframework.http.HttpStatus;

public class InvalidActionException extends GameException {
    public InvalidActionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
