package com.example.exboard.repository;

import com.example.exboard.entity.Board;
import com.example.exboard.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void insertReply() {

        IntStream.rangeClosed(1, 300).forEach(i -> {

            long bno = (long)(Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();

            Optional<Board> result = boardRepository.findById(bno);    //DB에 존재하는 번호인지 체크

            if ( !result.isEmpty()) {
                Reply reply = Reply.builder()
                        .text("Reply..." + i)
                        .board(board)
                        .replyer("guest")
                        .build();

                replyRepository.save(reply);
            }

        });
    }

    @Test
    public void testListByBoard() {
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(88L).build());

        replyList.forEach(reply -> System.out.println(reply));
    }
}
