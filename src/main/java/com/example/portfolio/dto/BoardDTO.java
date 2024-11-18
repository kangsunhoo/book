package com.example.portfolio.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class BoardDTO {
    private Long idx;           // 게시글의 ID
    private String userid;      // 작성자 ID (MemberEntity의 userid)
    private String title;       // 게시글 제목
    private String conte;       // 게시글 내용
    private int hit;            // 조회수
    private int likes;          // 좋아요 수
    private LocalDate regdate;  // 등록 날짜

    // Getter 및 Setter 메소드
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    // 기본 생성자
    public BoardDTO() {}

    public BoardDTO(Long idx, String userid, String title, int likes) {
        this.idx = idx;
        this.userid = userid;
        this.title = title;
        this.likes = likes;
    }
}

