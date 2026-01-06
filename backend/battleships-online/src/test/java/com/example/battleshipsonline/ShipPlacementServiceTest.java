package com.example.battleshipsonline;

import com.example.battleshipsonline.exception.ex.InvalidCoordinateException;
import com.example.battleshipsonline.model.*;
import com.example.battleshipsonline.model.enums.GameStatus;
import com.example.battleshipsonline.model.enums.ShipName;
import com.example.battleshipsonline.model.enums.ShipOrientation;
import com.example.battleshipsonline.service.BoardService;
import com.example.battleshipsonline.service.GameService;
import com.example.battleshipsonline.service.PlayerService;
import com.example.battleshipsonline.service.ShipPlacementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShipPlacementServiceTest {

    @Mock
    private GameService gameService;

    @Mock
    private PlayerService playerService;

    @Mock
    private BoardService boardService;

    @InjectMocks
    private ShipPlacementService shipPlacementService;

    private Game game;
    private BoardState boardState;

    @BeforeEach
    public void setup() {
        Player player1 = Player.builder().playerId(1L).username("Alice").build();
        when(playerService.findByPlayerId(1L)).thenReturn(player1);

        Board board1 = new Board();
        board1.setBoardId(1L);

        game = Game.builder()
                .gameId(UUID.randomUUID())
                .status(GameStatus.PLACING_SHIPS)
                .player1Id(1L)
                .player1BoardId(1L)
                .build();

        when(gameService.getPlayersBoard(game, player1)).thenReturn(board1);

        boardState = new BoardState(1L);
        when(boardService.loadBoardState(1L)).thenReturn(boardState);

        when(gameService.getGame(any(UUID.class))).thenReturn(game);
    }

    // Valid EQs
    @ParameterizedTest
    @CsvSource({
            "HORIZONTAL, CARRIER, 1, 1",
            "HORIZONTAL, CARRIER, 1, 6",
            "HORIZONTAL, CARRIER, 10, 1",
            "HORIZONTAL, CARRIER, 10, 6",
            "VERTICAL, CARRIER, 1, 1",
            "VERTICAL, CARRIER, 6, 1",
            "VERTICAL, CARRIER, 1, 10",
            "VERTICAL, CARRIER, 6, 10"
    })
    @DisplayName("Place ships with valid coordinates - VALID")
    public void testPlaceShip_Valid(String orientation, String name, int row, int col) {
        ShipOrientation shipOrientation = ShipOrientation.valueOf(orientation);
        ShipName shipName = ShipName.valueOf(name);
        Coordinate coordinate = new Coordinate(row, col);

        assertDoesNotThrow(() -> shipPlacementService.draftShipPlacement(game.getGameId(), 1L, shipName, shipOrientation, coordinate));

        for (int i = 0; i < 5; i++) {
            Coordinate c;
            if (shipOrientation == ShipOrientation.HORIZONTAL) {
                c = new Coordinate(row, col + i);
            } else {
                c = new Coordinate(row + i, col);
            }
            assertTrue(boardState.getCell(c).isDraftOccupied());
        }
    }

    // Invalid EQs - Out of bounds
    @ParameterizedTest
    @CsvSource({
            "HORIZONTAL, CARRIER, 0, 1",
            "HORIZONTAL, CARRIER, 1, 0",
            "VERTICAL, CARRIER, 10, 11",
            "VERTICAL, CARRIER, 11, 10",
            "HORIZONTAL, CARRIER, 1, 7",
            "HORIZONTAL, CARRIER, 10, 7",
            "VERTICAL, CARRIER, 7, 1",
            "VERTICAL, CARRIER, 7, 10"
    })
    @DisplayName("Place ships out of bounds - INVALID")
    public void testPlaceShipOutOfBounds_Invalid(String orientation, String name, int row, int col) {
        ShipOrientation shipOrientation = ShipOrientation.valueOf(orientation);
        ShipName shipName = ShipName.valueOf(name);
        Coordinate coordinate = new Coordinate(row, col);

        assertThrows(InvalidCoordinateException.class,
                () -> shipPlacementService.draftShipPlacement(game.getGameId(), 1L, shipName, shipOrientation, coordinate));
    }

    // Invalid EQs - Overlap
    @ParameterizedTest
    @CsvSource({
            "1, 1, HORIZONTAL, 1, 1, VERTICAL",
            "10, 6, HORIZONTAL, 6, 10, VERTICAL"
    })
    @DisplayName("Place ships that overlaps other ships - INVALID")
    public void testPlaceShipOverlap_Invalid(int row1, int col1, String orientation1, int row2, int col2, String orientation2) {
        ShipOrientation o1 = ShipOrientation.valueOf(orientation1);
        ShipOrientation o2 = ShipOrientation.valueOf(orientation2);
        Coordinate start1 = new Coordinate(row1, col1);
        Coordinate start2 = new Coordinate(row2, col2);

        shipPlacementService.draftShipPlacement(game.getGameId(), 1L, ShipName.CARRIER, o1, start1);

        assertThrows(InvalidCoordinateException.class,
                () -> shipPlacementService.draftShipPlacement(game.getGameId(), 1L, ShipName.CARRIER, o2, start2));
    }
}
