package com.example.battleshipsonline.exception.ex;

import com.example.battleshipsonline.exception.GameException;
import org.springframework.http.HttpStatus;

public class InvalidPlayerException extends GameException {
    public InvalidPlayerException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
