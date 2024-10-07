package com.example.portfolio.dto;

import lombok.Data;
import lombok.ToString;
import java.time.LocalDateTime;

@Data
@ToString
public class BookDTO {
    private Long bookId;         // 책 ID
    private String sbookFile;    // 책의 작은 파일
    private String obookFile;    // 책의 원본 파일
    private String bookTitle;    // 책 제목
    private String bookContent;  // 책 내용
    private String bookAuthor;   // 책 저자
    private String bookCompany;  // 출판사
    private Long categoryId;     // 카테고리 ID
    private String categoryName; // 카테고리 이름 (추가)
    private LocalDateTime regdate; // 등록 날짜 (추가)


}

