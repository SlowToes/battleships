package com.example.battleshipsonline.model;

import com.example.battleshipsonline.model.enums.ShipName;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int SIZE = 10;

    private final Cell[][] grid = new Cell[SIZE + 1][SIZE + 1]; // 1-indexed board
    private final List<Ship> ships = new ArrayList<>();

    public Board() {
        for (int i = 1; i <= SIZE; i++) {
            for (int j = 1; j <= SIZE; j++) {
                grid[i][j] = new Cell(false, false, null);
            }
        }

        ships.add(new Ship(ShipName.CARRIER,     5, new ArrayList<>(5), 0));
        ships.add(new Ship(ShipName.BATTLESHIP,  4, new ArrayList<>(4), 0));
        ships.add(new Ship(ShipName.DESTROYER,   3, new ArrayList<>(3), 0));
        ships.add(new Ship(ShipName.SUBMARINE,   3, new ArrayList<>(3), 0));
        ships.add(new Ship(ShipName.PATROL_BOAT, 2, new ArrayList<>(2), 0));
    }

    public Cell getCell(Coordinate c) {
        return grid[c.getRow()][c.getCol()];
    }

    public Ship getShip(ShipName name) {
        for (Ship ship : ships) {
            if (ship.getName() == name) {
                return ship;
            }
        }
        return null;
    }

    public Ship getShip(Coordinate c) {
        for (Ship ship : ships) {
            if (ship.getName() == getCell(c).getName()) {
                return ship;
            }
        }
        return null;
    }

    public boolean allShipsPlaced() {
        for (Ship ship : ships) {
            if (ship.getPosition().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }
}
