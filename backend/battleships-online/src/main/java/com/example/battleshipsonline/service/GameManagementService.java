package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.ex.InvalidGameException;
import com.example.battleshipsonline.model.Board;
import com.example.battleshipsonline.model.Game;
import com.example.battleshipsonline.model.enums.GameStatus;
import com.example.battleshipsonline.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameManagementService {

    private final GameRepository gameRepository;
    private final BoardService boardService;
    private final GameService gameService;
    private final SimpMessagingTemplate template;

    public Game createGame(Long playerId) {
        Board board = new Board();
        boardService.save(board);

        Game game = Game.builder()
                .status(GameStatus.NEW)
                .player1Id(playerId)
                .player1BoardId(board.getBoardId())
                .build();

        return gameRepository.save(game);
    }

    public void connectToGame(UUID gameId, Long playerId) {
        Game game = gameService.getGame(gameId);
        if (game.getPlayer1Id().equals(playerId)) {
            throw new InvalidGameException("You can't join your own game");
        }
        if (!gameService.isWaitingForPlayer(game)) {
            throw new InvalidGameException("Game is already full");
        }

        Board board = new Board();
        boardService.save(board);

        game.setPlayer2Id(playerId);
        game.setPlayer2BoardId(board.getBoardId());
        game.setStatus(GameStatus.PLACING_SHIPS);
        gameService.randomlyAssignFirstTurn(game);

        gameRepository.save(game);

        template.convertAndSend("/topic/game/" + gameId.toString() + "/ready", "READY");
    }
}
