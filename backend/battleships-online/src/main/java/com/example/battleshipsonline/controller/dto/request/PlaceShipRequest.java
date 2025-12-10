package com.example.battleshipsonline.controller.dto.request;

import com.example.battleshipsonline.model.Coordinate;
import com.example.battleshipsonline.model.enums.ShipName;
import com.example.battleshipsonline.model.enums.ShipOrientation;

public record PlaceShipRequest(String username, String shipName, String shipOrientation, String x, String y) {
    public ShipName getShipName() {
        return ShipName.valueOf(shipName);
    }

    public ShipOrientation getShipOrientation() {
        return ShipOrientation.valueOf(shipOrientation);
    }

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
