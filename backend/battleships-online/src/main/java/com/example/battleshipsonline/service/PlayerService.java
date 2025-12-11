package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.InvalidCredentialException;
import com.example.battleshipsonline.exception.InvalidPlayerException;
import com.example.battleshipsonline.model.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class PlayerService {
    private final Map<String, Player> players = new HashMap<>();
    private final Random random = new Random();

    public Player getPlayer(String username) {
        Player player = players.get(username);
        if (player == null) {
            throw new InvalidPlayerException("Player not found");
        }
        return player;
    }

    public Player loginAsGuest() {
        String username;
        do {
            username = "Guest" + random.nextInt(1_000_000);
        } while (players.containsKey(username));

        String password = "GuestPassword" + random.nextInt(1_000_000);

        Player player = new Player(username, password);
        players.put(username, player);
        return player;
    }

    public Player registerAsPlayer(String username, String password) {
        if (players.containsKey(username)) {
            throw new InvalidCredentialException("Username already exists");
        }

        Player player = new Player(username, password);
        players.put(username, player);
        return player;
    }

    public Player login(String username, String password) {
        Player player = players.get(username);

        if (player == null) {
            throw new InvalidCredentialException("Invalid username");
        }
        if (!player.getPassword().equals(password)) {
            throw new InvalidCredentialException("Invalid password");
        }
        return player;
    }
}
