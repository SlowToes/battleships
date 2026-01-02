package com.example.battleshipsonline.model;

import com.example.battleshipsonline.model.enums.ShipName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardState {

    public static final int SIZE = 10;

    private Long boardStateId;
    private Cell[][] grid =  new Cell[SIZE + 1][SIZE + 1];
    private List<Ship> ships = new ArrayList<>();

    public BoardState() {
        for (int row = 1; row <= SIZE; row++) {
            for (int col = 1; col <= SIZE; col++) {
                grid[row][col] = new Cell(false, false, false);
            }
        }
    }

    public BoardState(Long boardId) {
        this();
        this.boardStateId = boardId;

        ships.add(new Ship(ShipName.CARRIER,     5, new ArrayList<>(), new ArrayList<>(), 0));
        ships.add(new Ship(ShipName.BATTLESHIP,  4, new ArrayList<>(), new ArrayList<>(), 0));
        ships.add(new Ship(ShipName.DESTROYER,   3, new ArrayList<>(), new ArrayList<>(), 0));
        ships.add(new Ship(ShipName.SUBMARINE,   3, new ArrayList<>(), new ArrayList<>(), 0));
        ships.add(new Ship(ShipName.PATROL_BOAT, 2, new ArrayList<>(), new ArrayList<>(), 0));
    }

    public Cell getCell(Coordinate c) {
        return grid[c.getRow()][c.getCol()];
    }

    public Ship getShipByName(ShipName name) {
        for (Ship ship : ships) {
            if (ship.getName() == name) {
                return ship;
            }
        }
        return null;
    }

    public Ship getShipByCoordinate(Coordinate c) {
        for (Ship ship : ships) {
            if (ship.getPosition().contains(c)) {
                return ship;
            }
        }
        return null;
    }

    public boolean allDraftShipsPlaced() {
        for (Ship ship : ships) {
            if (ship.getDraftPosition().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void clearShipDraft(ShipName name) {
        for (Ship ship : ships) {
            if (ship.getName() == name) {
                ship.getDraftPosition().clear();
            }
        }
    }

    public void clearAllDrafts() {
        for (int row = 1; row <= SIZE; row++) {
            for (int col = 1; col <= SIZE; col++) {
                grid[row][col].setDraftOccupied(false);
            }
        }
        for (Ship ship : ships) {
            ship.getDraftPosition().clear();
        }
    }

    public void commitDrafts() {
        for (int row = 1; row <= SIZE; row++) {
            for (int col = 1; col <= SIZE; col++) {
                grid[row][col].commitDraft();
            }
        }
        for (Ship ship : ships) {
            ship.commitDraft();
        }
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
