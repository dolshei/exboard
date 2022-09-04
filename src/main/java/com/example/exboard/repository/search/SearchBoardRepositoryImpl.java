package com.example.exboard.repository.search;

import com.example.exboard.entity.Board;
import com.example.exboard.entity.QBoard;
import com.example.exboard.entity.QMember;
import com.example.exboard.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

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
        // log.info("search1.........");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //jpqlQuery.select(board).where(board.bno.eq(2L));
        //List<Board> result = jpqlQuery.fetch();

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        List<Tuple> result = tuple.fetch();

        return null;
    }

    /*
     * Pageable 의 Sort 객체는 JPQLQuery 의 orderBy()의 파라미터로 전달되어야 하지만 JPQL에서는
     * Sort 객체를 지원하지 않기 때문에 orderBy()의 경우 OrderSpecifier을 파라미터로 처리해야 한다.
     *
     * OrderSpecifier에는 정렬이 필요하므로 Sort 객체의 정렬 관련 정보를 Order타입으로 처리하고,
     * Sort 객체의 속성(bno, title)등은 PathBuilder로 처리한다.
     * PathBuilder를 생성할 때 문자열로 된 이름은 JPQLQuery를 생성할때 이용하는 변수명과 동일해야 한다.
     */
    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        booleanBuilder.and(expression);

        if (type != null) {
            String[] typeArr = type.split("");

            // 검색 조건 작성
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {
                switch (t){
                    case "t" :
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w" :
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c" :
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        // order by
        Sort sort = pageable.getSort();

        // tuple.orderBy(board.bno, desc());
        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC:Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(board);

        // page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        // count를 얻는 방법 : fetchCount() 이용
        // Pageable을 파라미터로 전달받은 이유 : JPQLQuery의 offSet()와 limit()를 이용해 페이지 처리를 하기 위해
        long count = tuple.fetchCount();

        // searchPage()의 리턴 타입은 Page<Object[]>타입으로 메서드 내부에서 Page 타입의 객체를 생성해야 한다.
        return new PageImpl<Object[]>(result.stream().map(tuple1 -> tuple1.toArray()).collect(Collectors.toList()),pageable,count);
    }


}
