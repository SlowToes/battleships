package com.example.battleshipsonline.controller;

import com.example.battleshipsonline.model.Game;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.service.GameManagementService;
import com.example.battleshipsonline.service.PlayerService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameManagementController {

    private final GameManagementService gameManagementService;
    private final PlayerService playerService;

    @PostMapping("/create")
    public GameResponse createGame(Principal principal) {
        Player player = playerService.findByUsername(principal.getName());
        Game game = gameManagementService.createGame(player.getPlayerId());

        return GameResponse.builder()
                .gameId(game.getGameId().toString())
                .build();
    }

    @PostMapping("/{gameId}/join")
    public GameResponse joinGame(@PathVariable String gameId, Principal principal) {
        Player player = playerService.findByUsername(principal.getName());
        gameManagementService.connectToGame(UUID.fromString(gameId), player.getPlayerId());

        return GameResponse.builder()
                .gameId(gameId)
                .build();
    }

    @Builder
    public record GameResponse(String gameId) {
    }
}
