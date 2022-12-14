package com.example.exboard.service;

import com.example.exboard.dto.BoardDTO;
import com.example.exboard.dto.PageRequestDTO;
import com.example.exboard.dto.PageResultDTO;
import com.example.exboard.entity.Board;
import com.example.exboard.entity.Member;
import com.example.exboard.repository.BoardRepository;
import com.example.exboard.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    @Autowired
    private final BoardRepository repository;       // 자동주입 final

    @Autowired
    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        Board board = dtoToEntity(dto);
        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0], (Member) en[1], (Long) en[2]));

        // Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    // 삭제, 트랜잭션 추가
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {

        // 댓글부터 삭제
        replyRepository.deleteByBno(bno);

        // 게시글 삭제
        repository.deleteById(bno);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        // getOne() : 필요한 순간까지 로딩을 지연하는 방식 을 getReferenceById() 로 변환
        Board board = repository.getReferenceById(boardDTO.getBno());

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        repository.save(board);
    }
}
