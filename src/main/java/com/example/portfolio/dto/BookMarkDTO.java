package com.example.portfolio.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookMarkDTO {

    private Long markIdx;  // 북마크 ID
    private String userid;  // 사용자 ID
    private Long bookId;    // 책 ID
    private LocalDateTime regDate;  // 등록 날짜

    // 생성자 및 변환 메소드 추가 가능
}
