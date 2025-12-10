package com.example.battleshipsonline.controller;

import com.example.battleshipsonline.controller.dto.request.FireRequest;
import com.example.battleshipsonline.controller.dto.response.FireResponse;
import com.example.battleshipsonline.model.FireResult;
import com.example.battleshipsonline.model.Game;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.model.enums.FireResultType;
import com.example.battleshipsonline.service.GameManagementService;
import com.example.battleshipsonline.service.GameplayService;
import com.example.battleshipsonline.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {
    private final GameManagementService gameManagementService;
    private final GameplayService gameplayService;
    private final PlayerService playerService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/game/{gameId}/fire")
    public void handleFire(@DestinationVariable String gameId, FireRequest request) {
        Player player = playerService.getPlayer(request.username());
        Game game = gameManagementService.getGame(gameId);

        FireResult result = gameplayService.fireShot(game, player, request.getCoordinate());

        FireResponse response = new FireResponse(
                result.getType().toString(),
                result.getSunkShipName() == null ? null : result.getSunkShipName().toString(),
                result.getType() == FireResultType.GAME_OVER ? "Game Over" : "In progress",
                result.getWinner() == null ? "None" : result.getWinner().getUsername()
        );

        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, response);
    }
}
