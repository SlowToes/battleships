package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.ex.InvalidPlayerException;
import com.example.battleshipsonline.exception.ex.InvalidGameException;
import com.example.battleshipsonline.model.Board;
import com.example.battleshipsonline.model.BoardState;
import com.example.battleshipsonline.model.Game;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final BoardService boardService;

    public Game getGame(UUID gameId) {
        Game game = gameRepository.findByGameId(gameId);
        if (game == null) {
            throw new InvalidGameException("Game not found");
        }
        return game;
    }

    public void save(Game game) {
        gameRepository.save(game);
    }

    public void randomlyAssignFirstTurn(Game game) {
        game.setPlayer1Turn(new Random().nextBoolean());
        gameRepository.save(game);
    }

    public boolean isWaitingForPlayer(Game game) {
        return game.getPlayer2Id() == null;
    }

    public Board getPlayersBoard(Game game, Player player) {
        if (player.getPlayerId().equals(game.getPlayer1Id())) {
            return boardService.findByBoardId(game.getPlayer1BoardId());
        }
        if (player.getPlayerId().equals(game.getPlayer2Id())) {
            return boardService.findByBoardId(game.getPlayer2BoardId());
        }
        throw new InvalidPlayerException("Player not part of this game");
    }

    public Board getOpponentBoard(Game game, Player player) {
        if (player.getPlayerId().equals(game.getPlayer1Id())) {
            return boardService.findByBoardId(game.getPlayer2BoardId());
        }
        if (player.getPlayerId().equals(game.getPlayer2Id())) {
            return boardService.findByBoardId(game.getPlayer1BoardId());
        }
        throw new InvalidPlayerException("Player not part of this game");
    }

    public void setReady(Game game, Player player) {
        if (player.getPlayerId().equals(game.getPlayer1Id())) {
            game.setPlayer1Ready(true);
        } else {
            game.setPlayer2Ready(true);
        }
        gameRepository.save(game);
    }

    public boolean bothPlayersReady(Game game) {
        return game.isPlayer1Ready() && game.isPlayer2Ready();
    }

    public boolean isPlayersTurn(Game game, Player player) {
        return (game.isPlayer1Turn() && player.getPlayerId().equals(game.getPlayer1Id())) ||
                (!game.isPlayer1Turn() && player.getPlayerId().equals(game.getPlayer2Id()));
    }

    public void switchTurn(Game game) {
        game.setPlayer1Turn(!game.isPlayer1Turn());
        gameRepository.save(game);
    }

    public boolean isGameOver(Game game) {
        BoardState boardState1 = boardService.loadBoardState(game.getPlayer1BoardId());
        BoardState boardState2 = boardService.loadBoardState(game.getPlayer2BoardId());
        return boardState1.allShipsSunk() || boardState2.allShipsSunk();
    }

    public Player getWinner(Game game) {
        BoardState boardState1 = boardService.loadBoardState(game.getPlayer1BoardId());
        BoardState boardState2 = boardService.loadBoardState(game.getPlayer2BoardId());
        if (boardState1.allShipsSunk()) return playerService.findByPlayerId(game.getPlayer2Id());
        if (boardState2.allShipsSunk()) return playerService.findByPlayerId(game.getPlayer1Id());
        return null;
    }
}
