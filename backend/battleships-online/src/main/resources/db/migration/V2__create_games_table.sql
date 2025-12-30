CREATE TABLE games
(
    game_id          UUID NOT NULL,
    status           VARCHAR(255),
    player1_id       BIGINT,
    player2_id       BIGINT,
    player1_board_id BIGINT,
    player2_board_id BIGINT,
    player1_turn     BOOLEAN,
    player1_ready    BOOLEAN,
    player2_ready    BOOLEAN,
    CONSTRAINT pk_games PRIMARY KEY (game_id)
);