package com.example.portfolio.service;

import com.example.portfolio.dto.CommentDTO;
import com.example.portfolio.dto.ReviewDTO;

import java.util.List;

// 리뷰 서비스의 인터페이스
public interface ReviewService {
    // 리뷰 추가 메서드
    void addReview(ReviewDTO reviewDTO);

    // 특정 책의 모든 리뷰 조회 메서드
    List<ReviewDTO> getReviewsByBookId(Long bookId);

    // 특정 사용자의 모든 리뷰 조회 메서드
    List<ReviewDTO> getReviewsByUserId(String userId);

    // 추가: 리뷰 삭제 메서드
    void deleteReview(Long idx);

    // 추가: 리뷰 ID로 리뷰 조회 메서드
    ReviewDTO getReviewById(Long idx);


    // 댓글 수정
    // Service Interface
    void updateReview(Long idx, ReviewDTO.Request reviewRequestDTO);



}
