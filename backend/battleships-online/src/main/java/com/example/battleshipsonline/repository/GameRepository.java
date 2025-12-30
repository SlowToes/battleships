package com.example.battleshipsonline.repository;

import com.example.battleshipsonline.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    Game findByGameId(UUID gameId);
}