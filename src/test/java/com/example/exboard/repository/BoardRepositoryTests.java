package com.example.exboard.repository;

import com.example.exboard.entity.Board;
import com.example.exboard.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class BoardRepositoryTests {

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void insertBoard() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member= Member.builder()
                    .email("user" + i + "@aaa.com")
                    .build();

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer(member)
                    .build();

            boardRepository.save(board);
        });
    }

    // LAZY Loading 방식으로 로딩하기 때문에 Board 테이블만 가져오는것은 문제가 없지만
    // board.getWriter() 메소드에서 문제가 발생한다. => board.getWriter()는 member 테이블을 로딩해야 하는데
    // 이미 DB 연결이 끝난 상태이므로 에러가 발생한다.
    // 해결방법 : @Transactional 어노테이션을 추가한다. => 해당 메소드를 하나의 트랜잭션으로 처리하라는 의미로
    // 필요할 때 다시 DB와 연결된다.
    @Test
    @Transactional
    public void testRead1() {
        Optional<Board> result = boardRepository.findById(100L);    //DB에 존재하는 번호

        Board board = result.get();

        System.out.println(board);
        System.out.println(board.getWriter());
    }

    @Test
    public void testReadWithWriter() {
        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr = (Object[]) result;

        System.out.println("--------------------------");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testGetBoardWithReply() {

        List<Object[]> result = boardRepository.getBoardWithReply(100L);

        for (Object[] arr2 : result) {
            System.out.println(Arrays.toString(arr2));
        }
    }

    @Test
    public void testGetBoardWithReplyCount() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row -> {
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void testGetBoardByBno() {
        Object result = boardRepository.getBoardByBno(100L);

        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testSearch1() {
        boardRepository.search1();
    }
}
