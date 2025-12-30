package com.example.battleshipsonline.repository;

import com.example.battleshipsonline.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByPlayerId(Long playerId);

    Player findByUsername(String username);

    boolean existsByUsername(String username);
}
