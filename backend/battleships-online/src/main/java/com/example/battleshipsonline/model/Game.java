package com.example.battleshipsonline.model;

import com.example.battleshipsonline.model.enums.GameStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "games")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "game_id")
    private UUID gameId;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "player1_id")
    private Long player1Id;

    @Column(name = "player2_id")
    private Long player2Id;

    @Column(name = "player1_board_id")
    private Long player1BoardId;

    @Column(name = "player2_board_id")
    private Long player2BoardId;

    @Column(name = "player1_turn")
    private boolean player1Turn;

    @Column(name = "player1_ready")
    private boolean player1Ready;

    @Column(name = "player2_ready")
    private boolean player2Ready;
}
