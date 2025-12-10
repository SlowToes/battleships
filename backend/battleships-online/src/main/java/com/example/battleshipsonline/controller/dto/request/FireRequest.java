package com.example.battleshipsonline.controller.dto.request;

import com.example.battleshipsonline.model.Coordinate;

public record FireRequest(String username, String x, String y) {
    public Coordinate getCoordinate() {
        return new Coordinate(getX(), getY());
    }

    public int getX() {
        return Integer.parseInt(x);
    }

    public int getY() {
        return Integer.parseInt(y);
    }
}
