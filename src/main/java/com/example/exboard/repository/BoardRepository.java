package com.example.exboard.repository;

import com.example.exboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // Entity Class 내부에 연관관계가 있는 경우 JPQL 처리
    // 한개의 로우(object) 내에 Object[]로 나옴.
    // Board를 사용하고 있지만 Member를 같이 조회해야 하는 상황에서 나옴.

    // @Query("SELECT b, w FROM Board b LEFT JOIN Member w ON b.writer = w.email WHERE b.bno =:bno")

    // 내부에 있는 Entity를 이용할 때는 LEFT JOIN 뒤에 ON을 이용하는 부분이 없다.
     @Query("SELECT b, w FROM Board b LEFT JOIN b.writer w WHERE b.bno =:bno")
     Object getBoardWithWriter(@Param("bno") Long bno);

    // Entity Class 내부에 연관관계가 없는 경우 JPQL 처리 (ON 사용)
    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno =:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    // 목록 화면에 필요한 데이터
    @Query(value = "SELECT b, w, COUNT(r) " +
            " FROM Board b " +
            " LEFT JOIN b.writer w " +
            " LEFT JOIN Reply r ON r.board = b " +
            " GROUP BY b",
            countQuery = "SELECT COUNT(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("SELECT b, w, COUNT(r) " +
            " FROM Board b LEFT JOIN b.writer w " +
            " LEFT OUTER JOIN Reply r ON r.board = b " +
            " WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
