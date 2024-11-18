package com.example.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Data
@ToString
public class ReviewDTO {
    private Long idx;         // 리뷰 ID
    private String userid;    // 사용자 ID
    private Long bookId;      // 책 ID
    private String review;     // 리뷰 내용
    private String nickname;   // 사용자 닉네임
    private LocalDateTime regdate; // 등록 날짜
    private Long reviewCount; // 추가된 필드: 리뷰 수

    // 생성자 추가
    public ReviewDTO(Long idx, String userid, Long bookId, String review, String nickname, LocalDateTime regdate) {
        this.idx = idx;
        this.userid = userid;
        this.bookId = bookId;
        this.review = review;
        this.nickname = nickname;
        this.regdate = regdate;
    }
    public ReviewDTO() {}
    // 내부 클래스 추가
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String review; // 댓글 수정 시 필요한 필드만 포함
    }

}

