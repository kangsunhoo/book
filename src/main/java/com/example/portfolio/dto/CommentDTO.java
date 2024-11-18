package com.example.portfolio.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private Long idx;           // 댓글 ID
    private String userid;      // 작성자 ID
    private Long boardIdx;      // 게시판 ID
    private String commentText;  // 댓글 내용 (필드 이름 수정)
    private String nickname;     // 사용자 닉네임
    private LocalDateTime regdate; // 작성일

    // 내부 클래스 추가
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String commentText; // 댓글 수정 시 필요한 필드만 포함
    }
}
