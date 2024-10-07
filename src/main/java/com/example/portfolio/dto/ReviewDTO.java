package com.example.portfolio.dto;

import lombok.Data;
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
}

