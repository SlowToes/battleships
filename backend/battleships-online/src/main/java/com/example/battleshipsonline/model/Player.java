package com.example.battleshipsonline.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}
