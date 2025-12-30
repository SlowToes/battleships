package com.example.battleshipsonline.service;

import com.example.battleshipsonline.exception.ex.BoardException;
import com.example.battleshipsonline.model.Board;
import com.example.battleshipsonline.model.BoardState;
import com.example.battleshipsonline.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ObjectMapper objectMapper;

    public BoardState loadBoardState(Long boardId) {
        Board board = boardRepository.findByBoardId(boardId);

        if (board.getBoardStateJson() == null || board.getBoardStateJson().isBlank()) {
            return new BoardState(board.getBoardId());
        }

        try {
            return objectMapper.readValue(board.getBoardStateJson(), BoardState.class);
        } catch (JacksonException e) {
            throw new BoardException("Corrupted board state");
        }
    }

    public void saveBoardState(Long boardId, BoardState state) {
        Board board = boardRepository.findByBoardId(boardId);

        try {
            board.setBoardStateJson(objectMapper.writeValueAsString(state));
            boardRepository.save(board);
        } catch (JacksonException e) {
            throw new BoardException("Failed to serialize board state");
        }
    }

    public void save(Board board) {
        boardRepository.save(board);
    }

    public Board findByBoardId(Long boardId) {
        Board board = boardRepository.findByBoardId(boardId);
        if  (board == null) {
            throw new BoardException("Board not found");
        }
        return board;
    }
}
