package com.example.battleshipsonline.exception.ex;

import com.example.battleshipsonline.exception.GameException;
import org.springframework.http.HttpStatus;

public class InvalidGameException extends GameException {
    public InvalidGameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
