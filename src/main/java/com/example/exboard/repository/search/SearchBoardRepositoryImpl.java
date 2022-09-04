package com.example.exboard.repository.search;

import com.example.exboard.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * 구현 클래스에서 가장 중요한 것 : QuerydslRepositorySupport 클래스를 상속해야 하는 것
 */
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    // QuerydslRepositorySupport 는 생성자가 존재하므로 클래스 내에서 super()를 이용해 호출해야 한다.
    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("search1...................");
        return null;
    }
}
