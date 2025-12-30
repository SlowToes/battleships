package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.ex.InvalidPlayerException;
import com.example.battleshipsonline.model.Player;
import com.example.battleshipsonline.repository.PlayerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PlayerService implements UserDetailsService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    @Override
    public @NullMarked UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player != null) {
            return User.builder()
                    .username(player.getUsername())
                    .password(player.getPassword())
                    .build();
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }

    public Player createGuest(HttpServletRequest request, HttpServletResponse response) {
        String username;
        do {
            username = "Guest" + random.nextInt(1_000_000);
        } while (playerRepository.existsByUsername(username));

        String rawPassword = "Password" + random.nextInt(1_000_000);

        Player player = Player.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .build();

        Player saved = playerRepository.save(player);

        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
        authenticate(request, response, auth);

        return saved;
    }

    public Player createPlayer(HttpServletRequest request, HttpServletResponse response, String username, String password) {
        Player player = Player.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        Player saved = playerRepository.save(player);

        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
        authenticate(request, response, auth);

        return saved;
    }

    public void authenticate(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, request, response);
    }

    public Player findByPlayerId(Long playerId) {
        Player player = playerRepository.findByPlayerId(playerId);
        if (player == null) {
            throw new InvalidPlayerException("Id not found");
        }
        return player;
    }
    
    public Player findByUsername(String username) {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new InvalidPlayerException("Username not found");
        }
        return player;
    }
}
