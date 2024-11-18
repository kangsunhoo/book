package com.example.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@ToString
@NoArgsConstructor
@Table(name = "comments")  // 테이블 이름 수정
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDX", unique = true, nullable = false)
    private Long idx; // 댓글 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false)
    private MemberEntity member; // 사용자 엔티티와의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx", referencedColumnName = "idx", nullable = false)
    private BoardEntity board; // 게시판 엔티티와의 관계

    @Column(name = "comment_text", nullable = false, length = 3500) // 필드 이름 수정
    private String commentText; // 댓글 내용

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname; // 사용자 닉네임

    @Column(name = "regdate", nullable = false)
    private LocalDateTime regdate; // 등록 날짜

    @PrePersist
    protected void onCreate() {
        regdate = LocalDateTime.now(); // 엔티티가 생성될 때 자동으로 현재 날짜 설정
    }
    public void  update(String commentText){
        this.commentText =commentText;
    }


}
