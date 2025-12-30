package com.example.battleshipsonline.exception.ex;

import com.example.battleshipsonline.exception.GameException;
import org.springframework.http.HttpStatus;

public class BoardException extends GameException {
    public BoardException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
