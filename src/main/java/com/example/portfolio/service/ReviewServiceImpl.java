package com.example.portfolio.service;

import com.example.portfolio.dto.CommentDTO;
import com.example.portfolio.dto.ReviewDTO;
import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.entity.CommentEntity;
import com.example.portfolio.entity.MemberEntity;
import com.example.portfolio.entity.ReviewEntity;
import com.example.portfolio.repository.MemberRepository;
import com.example.portfolio.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository; // 리뷰 데이터 접근을 위한 레포지토리
    private final MemberRepository memberRepository; // 회원 데이터 접근을 위한 레포지토리
    private final BookServiceImpl bookService; // 책 서비스

    // 리뷰 추가
    @Override
    public void addReview(ReviewDTO reviewDTO) {
        // 사용자와 책 엔티티 조회
        MemberEntity member = memberRepository.findByUserid(reviewDTO.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID")); // 잘못된 사용자 ID 처리
        BookEntity book = bookService.getBookById(reviewDTO.getBookId()); // 책 엔티티 조회

        // ReviewEntity 생성
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setMember(member); // 회원 정보 설정
        reviewEntity.setBook(book); // 책 정보 설정
        reviewEntity.setReview(reviewDTO.getReview()); // 리뷰 내용 설정
        reviewEntity.setNickname(reviewDTO.getNickname()); // 닉네임 설정

        // 리뷰 저장
        reviewRepository.save(reviewEntity);
    }

    // 특정 책의 모든 리뷰 조회
    @Override
    public List<ReviewDTO> getReviewsByBookId(Long bookId) {
        List<ReviewEntity> reviews = reviewRepository.findByBook_BookId(bookId); // 책 ID로 리뷰 조회
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList()); // DTO로 변환
    }

    // 특정 사용자의 모든 리뷰 조회
    @Override
    public List<ReviewDTO> getReviewsByUserId(String userId) {
        List<ReviewEntity> reviews = reviewRepository.findByMember_Userid(userId); // 사용자 ID로 리뷰 조회
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList()); // DTO로 변환
    }

    // ReviewEntity를 ReviewDTO로 변환
    private ReviewDTO convertToDTO(ReviewEntity review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setIdx(review.getIdx()); // 인덱스 설정
        dto.setUserid(review.getMember().getUserid()); // 사용자 ID 설정
        dto.setBookId(review.getBook().getBookId()); // 책 ID 설정
        dto.setReview(review.getReview()); // 리뷰 내용 설정
        dto.setNickname(review.getNickname()); // 닉네임 설정
        dto.setRegdate(review.getRegdate()); // 등록일 설정
        return dto;
    }
    // 추가: 리뷰 삭제 메서드
    @Override
    public void deleteReview(Long idx) {
        if (!reviewRepository.existsById(idx)) {
            throw new EntityNotFoundException("Review not found with id: " + idx);
        }
        reviewRepository.deleteById(idx);
    }

    // 추가: 리뷰 ID로 리뷰 조회 메서드
    @Override
    public ReviewDTO getReviewById(Long idx) {
        ReviewEntity review = reviewRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + idx));
        return convertToDTO(review);
    }

    @Override
    public void updateReview(Long idx, ReviewDTO.Request reviewRequestDTO) {
        ReviewEntity reviewEntity = reviewRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        reviewEntity.update(reviewRequestDTO.getReview());
        reviewRepository.save(reviewEntity);  // 이 줄 추가
    }




}
