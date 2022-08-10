package com.example.exboard.service;

import com.example.exboard.dto.BoardDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {

        BoardDTO dto = BoardDTO.builder()
                .title("Test...")
                .content("Content...")
                .writerEmail("user55@aaa.com")
                .build();

        Long bno = boardService.register(dto);

    }
}
