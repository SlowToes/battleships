package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.ex.InvalidActionException;
import com.example.battleshipsonline.exception.ex.InvalidCoordinateException;
import com.example.battleshipsonline.model.*;
import com.example.battleshipsonline.model.enums.GameStatus;
import com.example.battleshipsonline.model.enums.ShipName;
import com.example.battleshipsonline.model.enums.ShipOrientation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipPlacementService {

    private final GameService gameService;
    private final PlayerService playerService;
    private final BoardService boardService;
    private final SimpMessagingTemplate template;

    public void draftShipPlacement(UUID gameId, Long playerId, ShipName name, ShipOrientation orientation, Coordinate coordinate) {
        BoardState boardState = getBoardState(gameId, playerId);
        Ship ship = boardState.getShipByName(name);

        boardState.clearShipDraft(name);

        coordinate.validateCoordinates(orientation, ship.getSize());
        List<Coordinate> coordinates = calculateShipPosition(coordinate, orientation, ship.getSize());
        for (Coordinate c : coordinates) {
            if (boardState.getCell(c).isDraftOccupied()) {
                throw new InvalidCoordinateException("Ship overlaps another ship");
            }
        }

        for (Coordinate c : coordinates) {
            boardState.getCell(c).setDraftOccupied(true);
        }
        ship.setDraftPosition(coordinates);
        boardService.saveBoardState(boardState.getBoardStateId(), boardState);
    }

    public void clearAllDraftShipPlacements(UUID gameId, Long playerId) {
        BoardState boardState = getBoardState(gameId, playerId);
        boardState.clearAllDrafts();
        boardService.saveBoardState(boardState.getBoardStateId(), boardState);
    }

    public void setPlayerReady(UUID gameId, Long playerId) {
        BoardState boardState = getBoardState(gameId, playerId);

        if (!boardState.allDraftShipsPlaced()) {
            throw new InvalidActionException("Not all ships placed");
        }

        boardState.commitDrafts();

        if (boardState.allShipsPlaced()) {
            gameService.setReady(gameService.getGame(gameId), playerService.findByPlayerId(playerId));
            boardService.saveBoardState(boardState.getBoardStateId(), boardState);
        }
    }

    public void checkBothPlayersReady(UUID gameId) {
        Game game = gameService.getGame(gameId);
        if (gameService.bothPlayersReady(game)) {
            game.setStatus(GameStatus.IN_PROGRESS);
            gameService.save(game);

            template.convertAndSend("/topic/game/" + gameId.toString() + "/place-ships", "BOTH_READY");
        }
    }

    private BoardState getBoardState(UUID gameId, Long playerId) {
        Game game = gameService.getGame(gameId);
        if (game.getStatus() != GameStatus.PLACING_SHIPS) {
            throw new InvalidActionException("Ship placement phase is over");
        }

        Player player = playerService.findByPlayerId(playerId);
        Board board = gameService.getPlayersBoard(game, player);
        return boardService.loadBoardState(board.getBoardId());
    }

    private List<Coordinate> calculateShipPosition(Coordinate coordinate, ShipOrientation orientation, int size) {
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
