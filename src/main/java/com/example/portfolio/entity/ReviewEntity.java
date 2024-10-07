package com.example.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@ToString
@NoArgsConstructor
@Table(name = "review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDX", unique = true, nullable = false)
    private Long idx; // 리뷰 ID

    @Column(name = "USERID", nullable = false, length = 100)
    private String userid; // 사용자 ID

    @Column(name = "BOOKID", nullable = false)
    private Long bookId; // 책 ID

    @Column(name = "REVIEW", nullable = false, length = 3500)
    private String review; // 리뷰 내용

    @Column(name = "NICKNAME", nullable = false, length = 100)
    private String nickname; // 사용자 닉네임

    @Column(name = "REGDATE", nullable = false)
    private LocalDateTime regdate; // 등록 날짜

    @PrePersist
    protected void onCreate() {
        regdate = LocalDateTime.now(); // 엔티티가 생성될 때 날짜 설정
    }
}
