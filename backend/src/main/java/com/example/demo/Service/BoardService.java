package com.example.demo.Service;

import com.example.demo.Entity.Board;
import com.example.demo.Repository.BoardRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Transactional
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public Long addBoard(Board board){
        boardRepository.save(board);
        return board.getId();
    }

    public int updateView(Long boardId){
        return boardRepository.updateView(boardId);
    }
}
