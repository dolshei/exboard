package com.example.exboard.repository.search;

import com.example.exboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {
    Board search1();

    // 원하는 파라미터(Pageable)를 전송하고 Page<Object[]>를 만들어 반환한다.
    // PageRequestDTO 자체를 파라미터로 처리하지 않는 이유 : DTO를 가능하면 Repository 영역에서 다루지 않기 위해
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
