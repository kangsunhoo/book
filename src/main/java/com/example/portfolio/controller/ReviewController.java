package com.example.portfolio.controller;

import com.example.portfolio.dto.CommentDTO;
import com.example.portfolio.dto.ReviewDTO;
import com.example.portfolio.service.ReviewServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    // 리뷰 추가 폼 (옵션, 클라이언트 측에서 처리 가능)
    @GetMapping("/add")
    public String showAddReviewForm() {
        return "add_review"; // 리뷰 추가 페이지
    }

    // 리뷰 추가 처리
    @PostMapping("/add")
    public String addReview(
            @RequestParam("bookId") Long bookId,      // 책 ID를 파라미터로 받음
            @RequestParam("content") String content,    // 리뷰 내용을 파라미터로 받음
            HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        String userid = (String) session.getAttribute("userid");
        String nickname = (String) session.getAttribute("nickname"); // 닉네임이 세션에 저장되어 있다고 가정

        // 디버깅 로그 추가
        System.out.println("UserID: " + userid);
        System.out.println("Nickname: " + nickname);
        System.out.println("BookID: " + bookId);
        System.out.println("Content: " + content);

        if (userid == null) {
            return "redirect:/login"; // 로그인되지 않은 사용자라면 로그인 페이지로 리다이렉트
        }

        // ReviewDTO 객체 생성 및 설정
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setBookId(bookId);
        reviewDTO.setUserid(userid);
        reviewDTO.setNickname(nickname);
        reviewDTO.setReview(content);

        // 리뷰 추가
        reviewService.addReview(reviewDTO);

        // 리뷰가 추가된 책의 상세 페이지로 리다이렉트 (bookId 필요)
        return "redirect:/book_detail?bookId=" + bookId;
    }

    // 특정 책의 모든 리뷰 조회
    @GetMapping("/book/{bookId}")
    public String getReviewsByBook(@PathVariable Long bookId, Model model) {
        List<ReviewDTO> reviews = reviewService.getReviewsByBookId(bookId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("bookId", bookId);
        return "book_reviews"; // 리뷰 목록을 보여줄 페이지
    }

    // 특정 사용자의 모든 리뷰 조회 (옵션)
    @GetMapping("/user/{userId}")
    public String getReviewsByUser(@PathVariable String userId, Model model) {
        List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("userId", userId);
        return "user_reviews"; // 사용자 리뷰 목록을 보여줄 페이지
    }
    // 리뷰 삭제 처리
    @DeleteMapping("/delete/{idx}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long idx, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String userid = (String) session.getAttribute("userid");
        String role = (String) session.getAttribute("role"); // 세션에 role 정보가 있다고 가정

        // 삭제할 리뷰 조회
        ReviewDTO review = reviewService.getReviewById(idx);

        // 리뷰가 없을 경우 에러 응답
        if (review == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found 응답
        }

        // 관리자는 모든 댓글을 삭제 가능, 일반 사용자는 본인 댓글만 삭제 가능
        if (userid.equals(review.getUserid()) || "관리자".equals(role)) {
            reviewService.deleteReview(idx);
            return ResponseEntity.ok().build(); // 200 OK 응답
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden 응답
        }
    }
    @PutMapping("/update/{idx}")
    public ResponseEntity<?> updateReview(@PathVariable Long idx, @RequestBody ReviewDTO.Request request) {
        try {
            reviewService.updateReview(idx, request);
            ReviewDTO updateReview = reviewService.getReviewById(idx);
            return ResponseEntity.ok(updateReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 수정에 실패했습니다. 오류: " + e.getMessage());
        }
    }




}
