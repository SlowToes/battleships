package com.example.battleshipsonline.controller;

import com.example.battleshipsonline.model.Coordinate;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.service.GameplayService;
import com.example.battleshipsonline.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/play")
@RequiredArgsConstructor
public class GameplayController {

    private final GameplayService gameplayService;
    private final PlayerService playerService;

    @GetMapping("/me")
    public String getMyUsername(Principal principal) {
        return playerService.findByUsername(principal.getName()).getUsername();
    }

    @GetMapping("/{gameId}/initial")
    public String getStartingPlayerUsername(@PathVariable String gameId) {
        return gameplayService.getStartingPlayerUsername(UUID.fromString(gameId));
    }

    @GetMapping("/{gameId}/coordinates")
    public List<Coordinate> getStartingShipCoordinates(@PathVariable String gameId, Principal principal) {
        return gameplayService.getStartingShipCoordinates(UUID.fromString(gameId), playerService.findByUsername(principal.getName()));
    }

    @PostMapping("/{gameId}/fire")
    public void fireShot(@PathVariable String gameId, @RequestBody FireShotRequest request, Principal principal) {
        Player player = playerService.findByUsername(principal.getName());

        gameplayService.fireShot(
                UUID.fromString(gameId),
                player,
                request.getCoordinate()
        );
    }

    public record FireShotRequest(int row, int col) {

        public Coordinate getCoordinate() {
            return new Coordinate(row, col);
        }
    }
}
