package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.InvalidGameException;
import com.example.battleshipsonline.model.Board;
import com.example.battleshipsonline.model.Game;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.model.enums.GameStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameManagementService {
    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public Game createGame(Player player) {
        String gameId = UUID.randomUUID().toString().substring(0, 8);

        Game game = new Game();
        game.setGameId(gameId);
        game.setStatus(GameStatus.NEW);
        game.setPlayer1(player);
        game.setPlayer1Board(new Board());
        game.setTurn();

        games.put(gameId, game);
        return game;
    }

    public void connectToGame(String gameId, Player player) {
        Game game = getGame(gameId);
        if (game.getPlayer1().getUsername().equals(player.getUsername())) {
            throw new InvalidGameException("You can't join your own game.");
        }
        if (!game.isWaitingForPlayer()) {
            throw new InvalidGameException("Game is already full.");
        }

        game.setStatus(GameStatus.PLACING_SHIPS);
        game.setPlayer2(player);
        game.setPlayer2Board(new Board());
    }

    public Game getGame(String gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new InvalidGameException("Game not found.");
        }
        return game;
    }
}
