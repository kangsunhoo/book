package com.example.portfolio.controller;

import com.example.portfolio.dto.CommentDTO;
import com.example.portfolio.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;



    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@ModelAttribute CommentDTO commentDTO, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        String nickname = (String) session.getAttribute("nickname");

        commentDTO.setUserid(userid);
        commentDTO.setNickname(nickname);

        if (commentDTO.getCommentText() == null || commentDTO.getCommentText().trim().isEmpty()) {
            session.setAttribute("error", "댓글 내용을 입력하세요.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 내용을 입력하세요."); // 클라이언트에게 오류 메시지 전송
        }

        if (userid == null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/login") // 실제 로그인 페이지 경로로 설정
                    .build();
        }


        commentService.addComment(commentDTO);

        String redirectUrl = "/board/" + commentDTO.getBoardIdx();
        System.out.println("리다이렉트 URL: " + redirectUrl); // 리다이렉트 URL 로그 출력

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build(); // 리다이렉트 헤더 설정
    }





    // 특정 게시판의 댓글 목록 조회
    @GetMapping("/{boardIdx}")
    public String getComments(@PathVariable Long boardIdx, Model model) {
        List<CommentDTO> comments = commentService.getCommentsByBoardId(boardIdx);
        model.addAttribute("comments", comments);
        return "board/board_detail"; // 댓글 목록이 포함된 게시판 상세 페이지로 이동
    }

    @DeleteMapping("/delete/{idx}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long idx, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String userid = (String) session.getAttribute("userid");
        String role = (String) session.getAttribute("role"); // 세션에 role 정보가 있다고 가정

        // 삭제할 댓글 조회
        CommentDTO comment = commentService.getCommentById(idx);

        // 댓글이 없을 경우 에러 응답
        if (comment == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found 응답
        }

        // 관리자는 모든 댓글을 삭제 가능, 일반 사용자는 본인 댓글만 삭제 가능
        if (userid.equals(comment.getUserid()) || "관리자".equals(role)) {
            commentService.deleteComment(idx);
            return ResponseEntity.ok().build(); // 200 OK 응답
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden 응답
        }
    }

    @PutMapping("/update/{idx}")
    public ResponseEntity<?> updateComment(@PathVariable Long idx, @RequestBody CommentDTO.Request request) {
        try {
            commentService.updateComment(idx, request);
            CommentDTO updatedComment = commentService.getCommentById(idx);
            return ResponseEntity.ok(updatedComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 수정에 실패했습니다. 오류: " + e.getMessage());
        }
    }



}
