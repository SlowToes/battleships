package com.example.battleshipsonline.model;

import com.example.battleshipsonline.model.enums.ShipName;
import com.example.battleshipsonline.model.enums.ShipOrientation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ship {
    private ShipName name;
    private int size;
    private List<Coordinate> position;
    private int hitCount;

    public void hit() {
        hitCount++;
    }

    public boolean isSunk() {
        return hitCount == size;
    }

    public List<Coordinate> calculatePosition(Coordinate coordinate, ShipOrientation orientation) {
        List<Coordinate> coordinates = new ArrayList<>();
        int row = coordinate.getRow();
        int col = coordinate.getCol();

        for (int i = 0; i < size; i++) {
            int nRow = (orientation == ShipOrientation.VERTICAL)   ? row + i : row;
            int nCol = (orientation == ShipOrientation.HORIZONTAL) ? col + i : col;
            coordinates.add(new Coordinate(nRow, nCol));
        }
        return coordinates;
    }
}
