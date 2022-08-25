package com.example.exboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO를 구성하는 기준은 화면에 전달하는 데이터이거나 화면쪽에서 전달되는 데이터를 기준으로 하기 때문에
 * Entity 클래스의 구성과 일치하지 않는 경우가 많다.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    /*
    * BoardDTO의 경우 Member에 대한 참조는 구성하지 않고 작성한다.
    * BoardDTO 클래스와 Board Entity 클래스와 다른점 : Member를 참조하는 대신 화면에서 필요한 작성자의 이메일과 이름으로 처리하고 있는 점.
    * */

    private Long bno;
    private String title;
    private String content;
    private String writerEmail;         // 작성자의 이메일(id)
    private String writerName;          // 작성자의 이름
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount;             // 해당 게시글의 댓글 수
}
