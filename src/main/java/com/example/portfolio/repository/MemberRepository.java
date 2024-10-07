package com.example.portfolio.repository;


import com.example.portfolio.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Page<MemberEntity> findByUseridContaining(String keyword, Pageable pageable);
    Page<MemberEntity> findByNameContaining(String keyword, Pageable pageable);
    Page<MemberEntity> findByAge(String keyword, Pageable pageable);

    // List<MemberEntity> 대신 Optional<MemberEntity>로 수정
    Optional<MemberEntity> findByUserid(String userid);
}
