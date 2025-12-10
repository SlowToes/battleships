package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.InvalidActionException;
import com.example.battleshipsonline.model.*;
import com.example.battleshipsonline.model.enums.FireResultType;
import com.example.battleshipsonline.model.enums.GameStatus;
import com.example.battleshipsonline.model.enums.ShipName;
import com.example.battleshipsonline.model.enums.ShipOrientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FireShotTest {

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

    @Test
    public void testSinkCarrier_AllShotsHit() {
        gameplayService.placeShip(game, player1, ShipName.CARRIER, ShipOrientation.HORIZONTAL, new Coordinate(1, 1));
        gameplayService.placeShip(game, player2, ShipName.CARRIER, ShipOrientation.HORIZONTAL, new Coordinate(1, 1));
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setPlayer1Turn(true);

        for (int i = 1; i <= 4; i++) {
            FireResult result1 = gameplayService.fireShot(game, player1, new Coordinate(1, i));
            assertEquals(FireResultType.HIT, result1.getType());
            assertNull(result1.getSunkShipName());

            FireResult result2 = gameplayService.fireShot(game, player2, new Coordinate(1, i));
            assertEquals(FireResultType.HIT, result2.getType());
            assertNull(result2.getSunkShipName());
        }

        game.getPlayer2Board().getShip(ShipName.BATTLESHIP).setHitCount(4);
        game.getPlayer2Board().getShip(ShipName.DESTROYER).setHitCount(3);
        game.getPlayer2Board().getShip(ShipName.SUBMARINE).setHitCount(3);
        game.getPlayer2Board().getShip(ShipName.PATROL_BOAT).setHitCount(2);
        FireResult result1 = gameplayService.fireShot(game, player1, new Coordinate(1, 5));
        assertEquals(FireResultType.GAME_OVER, result1.getType());
        assertEquals(ShipName.CARRIER, result1.getSunkShipName());

        assertThrows(InvalidActionException.class,
                () -> gameplayService.fireShot(game, player2, new Coordinate(1, 5)));
    }

    @Test
    public void testSinkCarrier_Player1AllShotsMiss() {
        gameplayService.placeShip(game, player1, ShipName.CARRIER, ShipOrientation.HORIZONTAL, new Coordinate(1, 1));
        gameplayService.placeShip(game, player2, ShipName.CARRIER, ShipOrientation.HORIZONTAL, new Coordinate(1, 1));
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setPlayer1Turn(true);

        for (int i = 1; i <= 4; i++) {
            FireResult result1 = gameplayService.fireShot(game, player1, new Coordinate(2, i));
            assertEquals(FireResultType.MISS, result1.getType());
            assertNull(result1.getSunkShipName());

            FireResult result2 = gameplayService.fireShot(game, player2, new Coordinate(1, i));
            assertEquals(FireResultType.HIT, result2.getType());
            assertNull(result2.getSunkShipName());
        }

        FireResult result1 = gameplayService.fireShot(game, player1, new Coordinate(2, 5));
        assertEquals(FireResultType.MISS, result1.getType());
        assertNull(result1.getSunkShipName());

        game.getPlayer1Board().getShip(ShipName.BATTLESHIP).setHitCount(4);
        game.getPlayer1Board().getShip(ShipName.DESTROYER).setHitCount(3);
        game.getPlayer1Board().getShip(ShipName.SUBMARINE).setHitCount(3);
        game.getPlayer1Board().getShip(ShipName.PATROL_BOAT).setHitCount(2);
        FireResult result2 = gameplayService.fireShot(game, player2, new Coordinate(1, 5));
        assertEquals(FireResultType.GAME_OVER, result2.getType());
        assertEquals(ShipName.CARRIER, result2.getSunkShipName());
    }

    @Test
    public void testSink2Ships_Player2AllShotsMiss() {
        gameplayService.placeShip(game, player1, ShipName.CARRIER, ShipOrientation.HORIZONTAL, new Coordinate(1, 1));
        gameplayService.placeShip(game, player2, ShipName.DESTROYER, ShipOrientation.HORIZONTAL, new Coordinate(1, 1));
        gameplayService.placeShip(game, player2, ShipName.PATROL_BOAT, ShipOrientation.HORIZONTAL, new Coordinate(1, 4));
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setPlayer1Turn(true);

        FireResult result1 = gameplayService.fireShot(game, player1, new Coordinate(1, 1));
        assertEquals(FireResultType.HIT, result1.getType());
        assertNull(result1.getSunkShipName());

        FireResult result2 = gameplayService.fireShot(game, player2, new Coordinate(2, 1));
        assertEquals(FireResultType.MISS, result2.getType());
        assertNull(result2.getSunkShipName());

        FireResult result3 = gameplayService.fireShot(game, player1, new Coordinate(1, 2));
        assertEquals(FireResultType.HIT, result3.getType());
        assertNull(result3.getSunkShipName());

        FireResult result4 = gameplayService.fireShot(game, player2, new Coordinate(2, 2));
        assertEquals(FireResultType.MISS, result4.getType());
        assertNull(result4.getSunkShipName());

        FireResult result5 = gameplayService.fireShot(game, player1, new Coordinate(1, 3));
        assertEquals(FireResultType.SANK_SHIP, result5.getType());
        assertEquals(ShipName.DESTROYER, result5.getSunkShipName());

        FireResult result6 = gameplayService.fireShot(game, player2, new Coordinate(2, 3));
        assertEquals(FireResultType.MISS, result6.getType());
        assertNull(result6.getSunkShipName());

        FireResult result7 = gameplayService.fireShot(game, player1, new Coordinate(1, 4));
        assertEquals(FireResultType.HIT, result7.getType());
        assertNull(result7.getSunkShipName());

        FireResult result8 = gameplayService.fireShot(game, player2, new Coordinate(2, 4));
        assertEquals(FireResultType.MISS, result8.getType());
        assertNull(result8.getSunkShipName());

        game.getPlayer2Board().getShip(ShipName.CARRIER).setHitCount(5);
        game.getPlayer2Board().getShip(ShipName.BATTLESHIP).setHitCount(4);
        game.getPlayer2Board().getShip(ShipName.SUBMARINE).setHitCount(3);
        FireResult result9 = gameplayService.fireShot(game, player1, new Coordinate(1, 5));
        assertEquals(FireResultType.GAME_OVER, result9.getType());
        assertEquals(ShipName.PATROL_BOAT, result9.getSunkShipName());

        assertThrows(InvalidActionException.class,
                () -> gameplayService.fireShot(game, player2, new Coordinate(2, 5)));
    }
}
