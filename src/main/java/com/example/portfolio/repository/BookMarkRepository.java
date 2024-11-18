package com.example.portfolio.repository;

import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.entity.BookMarkEntity;
import com.example.portfolio.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMarkEntity, Long> {
    Optional<BookMarkEntity> findByUserAndBook(MemberEntity user, BookEntity book);

    @Query("SELECT b.book FROM BookMarkEntity b GROUP BY b.book ORDER BY COUNT(b.book) DESC")
    Page<BookEntity> findTopBookByBookmarks(Pageable pageable);



}
