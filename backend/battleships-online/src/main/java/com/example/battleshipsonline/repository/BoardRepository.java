package com.example.battleshipsonline.repository;

import com.example.battleshipsonline.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findByBoardId(Long boardId);
}