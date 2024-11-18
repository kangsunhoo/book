package com.example.portfolio.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bookmark")
public class BookMarkEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "MARKIDX", unique = true, nullable = false)
        private Long markIdx; // 북마크 ID

        @ManyToOne
        @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false)
        private MemberEntity user; // 사용자

        @ManyToOne
        @JoinColumn(name = "bookid", referencedColumnName = "bookId", nullable = false)
        private BookEntity book; // 책

        @Column(name = "REGDATE", nullable = false, updatable = false)
        private LocalDateTime regDate;

        @PrePersist
        protected void onCreate() {
                regDate = LocalDateTime.now();
        }

}

