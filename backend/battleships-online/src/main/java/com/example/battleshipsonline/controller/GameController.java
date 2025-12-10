package com.example.battleshipsonline.controller;

import com.example.battleshipsonline.controller.dto.request.GameRequest;
import com.example.battleshipsonline.controller.dto.request.PlaceShipRequest;
import com.example.battleshipsonline.controller.dto.response.GameResponse;
import com.example.battleshipsonline.model.Game;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.service.GameManagementService;
import com.example.battleshipsonline.service.GameplayService;
import com.example.battleshipsonline.service.PlayerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameManagementService gameManagementService;
    private final GameplayService gameplayService;
    private final PlayerService playerService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/create")
    public ResponseEntity<@NonNull GameResponse> createGame(@RequestBody GameRequest request) {
        Player player = playerService.getPlayer(request.username());
        Game game = gameManagementService.createGame(player);
        return ResponseEntity.ok(new GameResponse(game.getGameId(), "NEW", "Game created"));
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<@NonNull GameResponse> joinGame(@PathVariable String gameId, @RequestBody GameRequest request) {
        Player player = playerService.getPlayer(request.username());
        gameManagementService.connectToGame(gameId, player);

        simpMessagingTemplate.convertAndSend(
                "/topic/game/" + gameId,
                new GameResponse(gameId, "PLAYER_JOINED", player.getUsername() + " joined")
        );

        return ResponseEntity.ok(new GameResponse(gameId, "PLACING_SHIPS", "Joined game"));
    }

    @PostMapping("/{gameId}/ships")
    public ResponseEntity<@NonNull String> placeShip(@PathVariable String gameId, @RequestBody PlaceShipRequest request) {
        Player player = playerService.getPlayer(request.username());
        Game game = gameManagementService.getGame(gameId);
        gameplayService.placeShip(game, player, request.getShipName(), request.getShipOrientation(), request.getCoordinate());

        simpMessagingTemplate.convertAndSend(
                "/topic/game/" + gameId,
                request.username() + " placed a ship"
        );

        return ResponseEntity.ok("Ship placed");
    }
}
