package com.example.portfolio.repository;

import com.example.portfolio.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // 필요한 커스텀 쿼리 메서드가 있다면 여기에 추가

    // 등록일 기준 내림차순으로 게시글 목록을 가져오는 메서드
    List<BoardEntity> findAllByOrderByRegdateDesc();

    // 이전 게시글 조회: 현재 idx보다 작은 값 중 가장 큰 값
    Optional<BoardEntity> findTopByIdxLessThanOrderByIdxDesc(Long idx);

    // 다음 게시글 조회: 현재 idx보다 큰 값 중 가장 작은 값
    Optional<BoardEntity> findTopByIdxGreaterThanOrderByIdxAsc(Long idx);

    List<BoardEntity> findTop5ByOrderByLikesDesc();
}
