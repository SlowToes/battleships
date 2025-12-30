package com.example.battleshipsonline.controller;

import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.service.PlayerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/guest")
    public String createGuest(HttpServletRequest request, HttpServletResponse response) {
        Player guest = playerService.createGuest(request, response);
        return guest.getUsername();
    }

    @PostMapping("/signup")
    public String createPlayer(HttpServletRequest request, HttpServletResponse response, @RequestBody PlayerRequest playerRequest) {
        Player player = playerService.createPlayer(request, response, playerRequest.username(), playerRequest.password());
        return player.getUsername();
    }

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response, @RequestBody PlayerRequest playerRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(playerRequest.username(), playerRequest.password());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        // Set up security context
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authenticationResponse);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    public record PlayerRequest(String username, String password) {
    }
}
