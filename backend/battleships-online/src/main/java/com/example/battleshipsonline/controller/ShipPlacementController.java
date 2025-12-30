package com.example.battleshipsonline.controller;

import com.example.battleshipsonline.model.Coordinate;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.model.enums.ShipName;
import com.example.battleshipsonline.model.enums.ShipOrientation;
import com.example.battleshipsonline.service.PlayerService;
import com.example.battleshipsonline.service.ShipPlacementService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class ShipPlacementController {

    private final ShipPlacementService shipPlacementService;
    private final PlayerService playerService;

    @PostMapping("/{gameId}/ships/place")
    public PlaceShipResponse placeShip(@PathVariable String gameId, @RequestBody PlaceShipRequest request, Principal principal) {
        Player player = playerService.findByUsername(principal.getName());

        shipPlacementService.draftShipPlacement(
                UUID.fromString(gameId),
                player.getPlayerId(),
                request.getShipName(),
                request.getShipOrientation(),
                request.getCoordinate()
        );

        return PlaceShipResponse.builder()
                .gameId(gameId)
                .status("PLACED")
                .build();
    }

    @PostMapping("/{gameId}/ships/reset")
    public PlaceShipResponse resetPlacement(@PathVariable String gameId, Principal principal) {
        Player player = playerService.findByUsername(principal.getName());

        shipPlacementService.clearAllDraftShipPlacements(
                UUID.fromString(gameId),
                player.getPlayerId()
        );

        return PlaceShipResponse.builder()
                .gameId(gameId)
                .status("RESET")
                .build();
    }

    @PostMapping("/{gameId}/ships/ready")
    public PlaceShipResponse setPlayerReady(@PathVariable String gameId, Principal principal) {
        Player player = playerService.findByUsername(principal.getName());

        shipPlacementService.setPlayerReady(
                UUID.fromString(gameId),
                player.getPlayerId()
        );

        shipPlacementService.checkBothPlayersReady(UUID.fromString(gameId));

        return PlaceShipResponse.builder()
                .gameId(gameId)
                .status("PLAYER_READY")
                .build();
    }

    public record PlaceShipRequest(String shipName, String shipOrientation, int row, int col) {

        public ShipName getShipName() {
            return ShipName.valueOf(shipName);
        }

        public ShipOrientation getShipOrientation() {
            return ShipOrientation.valueOf(shipOrientation);
        }

        public Coordinate getCoordinate() {
            return new Coordinate(row, col);
        }
    }

    @Builder
    public record PlaceShipResponse(String gameId, String status) {
    }
}
