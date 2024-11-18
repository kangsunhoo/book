package com.example.portfolio.repository;

import com.example.portfolio.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 작성
    Page<BookEntity> findByBookTitleContaining(String title, Pageable pageable); // 제목 검색
    Page<BookEntity> findByBookAuthorContaining(String author, Pageable pageable); // 작가 검색
    List<BookEntity> findTop3ByCategory_CategoryNameOrderByRegdateDesc(String categoryName, Pageable pageable);
    List<BookEntity> findByCategory_CategoryIdOrderByRegdateDesc(Long categoryId);
    Optional<BookEntity> findTopByBookIdLessThanOrderByBookIdDesc(Long bookId);
    Optional<BookEntity> findTopByBookIdGreaterThanOrderByBookIdAsc(Long bookId);
}

