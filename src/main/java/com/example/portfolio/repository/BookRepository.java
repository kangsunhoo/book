package com.example.portfolio.repository;

import com.example.portfolio.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 작성
    List<BookEntity> findByCategory_CategoryId(Long categoryId);
    Page<BookEntity> findByBookTitleContaining(String title, Pageable pageable);
}

