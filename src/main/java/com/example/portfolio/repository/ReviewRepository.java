package com.example.portfolio.repository;

import com.example.portfolio.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    // 특정 책에 대한 모든 리뷰를 가져오는 메소드
    List<ReviewEntity> findByBook_BookId(Long bookId);

    // 특정 유저가 작성한 리뷰를 가져오는 메소드
    List<ReviewEntity> findByMember_Userid(String userid);

    // 리뷰 ID로 리뷰 조회
    ReviewEntity findByIdx(Long idx);


}

