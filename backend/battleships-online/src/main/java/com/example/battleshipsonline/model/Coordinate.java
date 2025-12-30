package com.example.battleshipsonline.model;

import com.example.battleshipsonline.exception.ex.InvalidCoordinateException;
import com.example.battleshipsonline.model.enums.ShipOrientation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.battleshipsonline.model.BoardState.SIZE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Coordinate {

    private int row;
    private int col;

    public void validateCoordinates(ShipOrientation orientation, int shipSize) {
        if (row < 1 || row > SIZE || col < 1 || col > SIZE) {
            throw new InvalidCoordinateException("Coordinates out of bounds");
        }

        if (orientation == ShipOrientation.HORIZONTAL && col + shipSize - 1 > SIZE) {
            throw new InvalidCoordinateException("Ship goes outside the board horizontally");
        }

        if (orientation == ShipOrientation.VERTICAL && row + shipSize - 1 > SIZE) {
            throw new InvalidCoordinateException("Ship goes outside the board vertically");
        }
    }
}
