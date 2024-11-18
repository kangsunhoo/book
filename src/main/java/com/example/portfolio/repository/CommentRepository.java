package com.example.portfolio.repository;

import com.example.portfolio.entity.CommentEntity;
import com.example.portfolio.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {


        // 특정 게시판 ID로 댓글 조회
        List<CommentEntity> findByBoardIdx(Long boardIdx);

        // 특정 사용자 ID로 댓글 조회
        List<CommentEntity> findByMember_Userid(String userid);  // 수정된 부분

        // 리뷰 ID로 리뷰 조회
        CommentEntity findByIdx(Long idx);


}
