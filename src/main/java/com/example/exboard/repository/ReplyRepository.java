package com.example.exboard.repository;

import com.example.exboard.entity.Board;
import com.example.exboard.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // Board 글 삭제시 댓글들 삭제
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bno =:bno ")
    void deleteByBno(Long bno);

    // 게시물로 댓글 목록 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
