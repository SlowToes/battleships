package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.InvalidActionException;
import com.example.battleshipsonline.exception.InvalidCoordinateException;
import com.example.battleshipsonline.model.*;
import com.example.battleshipsonline.model.enums.GameStatus;
import com.example.battleshipsonline.model.enums.ShipName;
import com.example.battleshipsonline.model.enums.ShipOrientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ShipPlacementTest {

    @Autowired
    private GameplayService gameplayService;

    @Autowired
    private GameManagementService gameManagementService;

    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setup() {
        player1 = new Player("Alice", "123");
        player2 = new Player("Bob", "456");
        game = gameManagementService.createGame(player1);
        gameManagementService.connectToGame(game.getGameId(), player2);
    }

    // Valid EQs
    @ParameterizedTest
    @CsvSource({
            "HORIZONTAL, 1, 1",
            "HORIZONTAL, 1, 6",
            "HORIZONTAL, 10, 1",
            "HORIZONTAL, 10, 6",
            "VERTICAL, 1, 1",
            "VERTICAL, 6, 1",
            "VERTICAL, 1, 10",
            "VERTICAL, 6, 10"
    })
    public void testPlaceShip_Valid(String orientation, int row, int col) {
        ShipOrientation shipOrientation = ShipOrientation.valueOf(orientation);
        Coordinate coordinate = new Coordinate(row, col);

        assertDoesNotThrow(() -> gameplayService.placeShip(game, player1, ShipName.CARRIER, shipOrientation, coordinate));

        Board board = game.getPlayersBoard(player1);
        for (int i = 0; i < 5; i++) {
            Coordinate c;
            if (shipOrientation == ShipOrientation.HORIZONTAL) {
                c = new Coordinate(row, col + i);
            } else {
                c = new Coordinate(row + i, col);
            }
            assertTrue(board.getCell(c).isOccupied());
        }
    }

    @Test
    public void testPlaceShip_AllShipsPlaced_ValidStatus() {
        gameplayService.placeShip(game, player1, ShipName.CARRIER, ShipOrientation.HORIZONTAL, new Coordinate(1, 1));
        gameplayService.placeShip(game, player1, ShipName.BATTLESHIP, ShipOrientation.HORIZONTAL, new Coordinate(2, 1));
        gameplayService.placeShip(game, player1, ShipName.DESTROYER, ShipOrientation.HORIZONTAL, new Coordinate(3, 1));
        gameplayService.placeShip(game, player1, ShipName.SUBMARINE, ShipOrientation.HORIZONTAL, new Coordinate(4, 1));
        gameplayService.placeShip(game, player1, ShipName.PATROL_BOAT, ShipOrientation.HORIZONTAL, new Coordinate(5, 1));

        gameplayService.placeShip(game, player2, ShipName.CARRIER, ShipOrientation.VERTICAL, new Coordinate(1, 1));
        gameplayService.placeShip(game, player2, ShipName.BATTLESHIP, ShipOrientation.VERTICAL, new Coordinate(1, 2));
        gameplayService.placeShip(game, player2, ShipName.DESTROYER, ShipOrientation.VERTICAL, new Coordinate(1, 3));
        gameplayService.placeShip(game, player2, ShipName.SUBMARINE, ShipOrientation.VERTICAL, new Coordinate(1, 4));
        gameplayService.placeShip(game, player2, ShipName.PATROL_BOAT, ShipOrientation.VERTICAL, new Coordinate(1, 5));

        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    public void testPlaceShip_InvalidStatus() {
        game.setStatus(GameStatus.NEW);
        assertThrows(InvalidActionException.class,
                () -> gameplayService.placeShip(game, player1, ShipName.CARRIER, ShipOrientation.HORIZONTAL, new Coordinate(1, 1)));
    }

    // Invalid EQs - Out of bounds
    @ParameterizedTest
    @CsvSource({
            "HORIZONTAL, 0, 1",
            "HORIZONTAL, 1, 0",
            "VERTICAL, 10, 11",
            "VERTICAL, 11, 10",
            "HORIZONTAL, 1, 7",
            "HORIZONTAL, 10, 7",
            "VERTICAL, 7, 1",
            "VERTICAL, 7, 10"
    })
    public void testPlaceShipOutOfBounds_Invalid(String orientation, int row, int col) {
        ShipOrientation shipOrientation = ShipOrientation.valueOf(orientation);
        Coordinate coordinate = new Coordinate(row, col);

        assertThrows(InvalidCoordinateException.class,
                () -> gameplayService.placeShip(game, player1, ShipName.CARRIER, shipOrientation, coordinate));
    }

    // Invalid EQs - Overlap
    @ParameterizedTest
    @CsvSource({
            "1, 1, HORIZONTAL, 1, 1, VERTICAL",
            "10, 6, HORIZONTAL, 6, 10, VERTICAL"
    })
    public void testPlaceShipOverlap_Invalid(int row1, int col1, String orientation1,
                                             int row2, int col2, String orientation2) {
        ShipOrientation o1 = ShipOrientation.valueOf(orientation1);
        ShipOrientation o2 = ShipOrientation.valueOf(orientation2);
        Coordinate start1 = new Coordinate(row1, col1);
        Coordinate start2 = new Coordinate(row2, col2);

        gameplayService.placeShip(game, player1, ShipName.CARRIER, o1, start1);

        assertThrows(InvalidCoordinateException.class,
                () -> gameplayService.placeShip(game, player1, ShipName.CARRIER, o2, start2));
    }
}
