package com.example.exboard.service;

import com.example.exboard.dto.BoardDTO;
import com.example.exboard.dto.PageRequestDTO;
import com.example.exboard.dto.PageResultDTO;
import com.example.exboard.entity.Board;
import com.example.exboard.entity.Member;

public interface BoardService {

    Long register(BoardDTO dto);

    default Board dtoToEntity(BoardDTO dto) {
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        return Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
    }

    // 목록 처리
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    default BoardDTO entityTODTO(Board board, Member member, Long replyCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue())  // Long 으로 나오므로 int로 처리
                .build();

        return boardDTO;
    }

    // 게시물 조회
    BoardDTO get(Long bno);
}
