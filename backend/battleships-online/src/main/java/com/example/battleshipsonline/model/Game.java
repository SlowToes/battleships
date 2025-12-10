package com.example.battleshipsonline.model;

import com.example.battleshipsonline.model.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {
    private final Random random = new Random();
    private String gameId;
    private GameStatus status;
    private Player player1;
    private Player player2;
    private Board player1Board;
    private Board player2Board;
    private boolean player1Turn;

    public void setTurn() {
        player1Turn = random.nextBoolean();
    }

    public boolean isWaitingForPlayer() {
        return player2 == null;
    }

    public Board getPlayersBoard(Player player) {
        return player.equals(player1) ? player1Board : player2Board;
    }

    public Board getOpponentBoard(Player player) {
        return player.equals(player1) ? player2Board : player1Board;
    }

    public boolean isPlayersTurn(Player player) {
        return (player1Turn && player.equals(player1)) || (!player1Turn && player.equals(player2));
    }

    public void switchTurn() {
        player1Turn = !player1Turn;
    }

    public boolean isGameOver() {
        return player1Board.allShipsSunk() || player2Board.allShipsSunk();
    }

    public Player getWinner() {
        return player1Board.allShipsSunk() ? player2 : player1;
    }
}
