package com.example.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@ToString
@NoArgsConstructor
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKID", unique = true, nullable = false)
    private Long bookId; // 책 ID

    @Column(name = "SBOOK_FILE", nullable = false, length = 100)
    private String sbookFile; // 책의 작은 파일 경로

    @Column(name = "OBOOK_FILE", nullable = false, length = 100)
    private String obookFile; // 책의 원본 파일 경로

    @Column(name = "BOOKTITLE", nullable = false, length = 255)
    private String bookTitle; // 책 제목

    @Column(name = "BOOKCONTENT", nullable = false, length = 3500)
    private String bookContent; // 책 내용

    @Column(name = "BOOKAUTHOR", nullable = false, length = 100)
    private String bookAuthor; // 책 저자

    @Column(name = "BOOKCOMPANY", nullable = false, length = 100)
    private String bookCompany; // 출판사

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORYID", nullable = false)
    private CategoryEntity category; // 카테고리

    // 생성 시점 자동 설정 (필요 시 사용)
    @Column(name = "REGDATE")
    private LocalDateTime regdate;

    @Column(name = "thumbnail_file")
    private String thumbnailFile;

    // Getter 및 Setter 추가
    public String getThumbnailFile() {
        return thumbnailFile;
    }

    public void setThumbnailFile(String thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }



}
