package com.example.battleshipsonline.controller;

import com.example.battleshipsonline.controller.dto.request.LoginRequest;
import com.example.battleshipsonline.controller.dto.request.RegisterRequest;
import com.example.battleshipsonline.controller.dto.response.PlayerResponse;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.service.PlayerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping("/register/guest")
    public ResponseEntity<@NonNull String> registerAsGuest() {
        String username = playerService.loginAsGuest();
        return ResponseEntity.ok("Login as " + username + " successful");
    }

    @PostMapping("/register/player")
    public ResponseEntity<@NonNull PlayerResponse> registerAsPlayer(@RequestBody RegisterRequest request) {
        Player player = playerService.registerAsPlayer(request.username(), request.password());
        return ResponseEntity.ok(new PlayerResponse(player.getUsername(), player.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull String> login(@RequestBody LoginRequest request) {
        playerService.login(request.username(), request.password());
        return ResponseEntity.ok("Login successful");
    }
}
