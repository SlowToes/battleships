package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.InvalidActionException;
import com.example.battleshipsonline.exception.InvalidCoordinateException;
import com.example.battleshipsonline.exception.InvalidGameException;
import com.example.battleshipsonline.model.*;
import com.example.battleshipsonline.model.enums.FireResultType;
import com.example.battleshipsonline.model.enums.GameStatus;
import com.example.battleshipsonline.model.enums.ShipName;
import com.example.battleshipsonline.model.enums.ShipOrientation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameplayService {
    public void placeShip(Game game, Player player, ShipName name, ShipOrientation orientation, Coordinate coordinate) {
        if (game.getStatus() != GameStatus.PLACING_SHIPS) {
            throw new InvalidActionException("Ship placement phase is over");
        }

        Board board = game.getPlayersBoard(player);
        Ship ship = board.getShip(name);

        coordinate.validateCoordinates(orientation, ship.getSize());

        List<Coordinate> coordinates = ship.calculatePosition(coordinate, orientation);

        for (Coordinate c : coordinates) {
            if (board.getCell(c).isOccupied()) {
                throw new InvalidCoordinateException("Ship overlaps another ship");
            }
        }

        for (Coordinate c : coordinates) {
            board.getCell(c).setOccupied(true);
            board.getCell(c).setName(name);
        }
        ship.setPosition(coordinates);

        if (game.getPlayer1Board().allShipsPlaced() && game.getPlayer2Board().allShipsPlaced()) {
            game.setStatus(GameStatus.IN_PROGRESS);
        }
    }

    public FireResult fireShot(Game game, Player player, Coordinate coordinate) {
        if (game == null) {
            throw new InvalidGameException("Game not found");
        }
        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new InvalidActionException("Game is over");
        }
        if (!game.isPlayersTurn(player)) {
            throw new InvalidActionException("Not your turn");
        }

        Board opponentBoard = game.getOpponentBoard(player);
        Cell cell =  opponentBoard.getCell(coordinate);

        if (cell.isHit()) {
            throw new InvalidActionException("Cell is already hit");
        }
        cell.setHit(true);

        Ship ship = opponentBoard.getShip(coordinate);

        if (ship != null) {
            ship.hit();
        }

        boolean hit = cell.isOccupied();
        boolean sunk = hit && ship != null && ship.isSunk();
        boolean gameOver = sunk && game.isGameOver();

        FireResultType type;
        ShipName sunkShipName = null;
        Player winner = null;
        if (gameOver) {
            type = FireResultType.GAME_OVER;
            sunkShipName = ship.getName();
            winner = game.getWinner();
            game.setStatus(GameStatus.FINISHED);
        }
        else if (sunk) {
            type = FireResultType.SANK_SHIP;
            sunkShipName = ship.getName();
            game.switchTurn();
        }
        else if (hit) {
            type = FireResultType.HIT;
            game.switchTurn();
        }
        else {
            type = FireResultType.MISS;
            game.switchTurn();
        }
        return new FireResult(type, sunkShipName, winner);
    }
}
