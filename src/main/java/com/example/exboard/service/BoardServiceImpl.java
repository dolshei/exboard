package com.example.exboard.service;

import com.example.exboard.dto.BoardDTO;
import com.example.exboard.entity.Board;
import com.example.exboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    @Autowired
    private final BoardRepository repository;

    @Override
    public Long register(BoardDTO dto) {

        Board board = dtoToEntity(dto);
        repository.save(board);

        return board.getBno();
    }
}
