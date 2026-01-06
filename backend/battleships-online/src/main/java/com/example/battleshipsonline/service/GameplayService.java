package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.ex.InvalidActionException;
import com.example.battleshipsonline.model.*;
import com.example.battleshipsonline.model.enums.FireResultType;
import com.example.battleshipsonline.model.enums.GameStatus;
import com.example.battleshipsonline.model.enums.ShipName;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameplayService {

    private final GameService gameService;
    private final PlayerService playerService;
    private final BoardService boardService;
    private final SimpMessagingTemplate template;

    public String getOpponentUsername(UUID gameId, Player player) {
        Game game = gameService.getGame(gameId);
        if (game.getPlayer1Id().equals(player.getPlayerId())) {
            return playerService.findByPlayerId(game.getPlayer2Id()).getUsername();
        } else {
            return playerService.findByPlayerId(game.getPlayer1Id()).getUsername();
        }
    }

    public String getStartingPlayerUsername(UUID gameId) {
        Game game = gameService.getGame(gameId);
        return getCurrentTurnPlayerUsername(game);
    }

    public List<Coordinate> getStartingShipCoordinates(UUID gameId, Player player) {
        Game game = gameService.getGame(gameId);
        Board board = gameService.getPlayersBoard(game, player);
        BoardState state = boardService.loadBoardState(board.getBoardId());

        List<Coordinate> coordinates = new ArrayList<>();
        for (Ship ship : state.getShips()) {
            coordinates.addAll(ship.getPosition());
        }
        return coordinates;
    }

    @Transactional
    public void fireShot(UUID gameId, Player player, Coordinate coordinate) {
        Game game = gameService.getGame(gameId);

        validateFire(game, player);
        ShotContext context = processShot(game, player, coordinate);
        FireResult result = resolveResult(game, context, coordinate);

        template.convertAndSend("/topic/game/" + gameId + "/update", result);
    }

    private void validateFire(Game game, Player player) {
        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new InvalidActionException("Game is over");
        }
        if (!gameService.isPlayersTurn(game, player)) {
            throw new InvalidActionException("Not your turn");
        }
    }

    private ShotContext processShot(Game game, Player player, Coordinate coordinate) {
        Board opponentBoard = gameService.getOpponentBoard(game, player);
        BoardState state = boardService.loadBoardState(opponentBoard.getBoardId());

        Cell cell = state.getCell(coordinate);
        if (cell.isHit()) {
            throw new InvalidActionException("Cell is already hit");
        }
        cell.setHit(true);

        Ship ship = state.getShipByCoordinate(coordinate);
        if (ship != null) {
            ship.hit();
        }

        boardService.saveBoardState(opponentBoard.getBoardId(), state);
        return new ShotContext(cell, ship);
    }

    private FireResult resolveResult(Game game, ShotContext context, Coordinate coordinate) {
        boolean hit = context.cell.isOccupied();
        boolean sunk = hit && context.ship != null && context.ship.isSunk();
        boolean gameOver = sunk && gameService.isGameOver(game);

        if (gameOver) {
            game.setStatus(GameStatus.FINISHED);
            gameService.save(game);
            return FireResult.builder()
                    .coordinate(coordinate)
                    .fireResultType(FireResultType.GAME_OVER)
                    .sunkShipName(context.ship.getName())
                    .currentPlayerUsername(getCurrentTurnPlayerUsername(game))
                    .winnerUsername(gameService.getWinner(game).getUsername())
                    .build();
        }

        if (sunk) {
            return FireResult.builder()
                    .coordinate(coordinate)
                    .fireResultType(FireResultType.SANK_SHIP)
                    .sunkShipName(context.ship.getName())
                    .currentPlayerUsername(getCurrentTurnPlayerUsername(game))
                    .winnerUsername(null)
                    .build();
        }

        if (hit) {
            return FireResult.builder()
                    .coordinate(coordinate)
                    .fireResultType(FireResultType.HIT)
                    .sunkShipName(null)
                    .currentPlayerUsername(getCurrentTurnPlayerUsername(game))
                    .winnerUsername(null)
                    .build();
        }

        gameService.switchTurn(game);
        return FireResult.builder()
                .coordinate(coordinate)
                .fireResultType(FireResultType.MISS)
                .sunkShipName(null)
                .currentPlayerUsername(getCurrentTurnPlayerUsername(game))
                .winnerUsername(null)
                .build();
    }

    private String getCurrentTurnPlayerUsername(Game game) {
        return game.isPlayer1Turn() ?
                playerService.findByPlayerId(game.getPlayer1Id()).getUsername() : playerService.findByPlayerId(game.getPlayer2Id()).getUsername();
    }

    private record ShotContext(Cell cell, Ship ship) {
    }

    @Builder
    public record FireResult(Coordinate coordinate, FireResultType fireResultType, ShipName sunkShipName, String currentPlayerUsername, String winnerUsername) {
    }
}
