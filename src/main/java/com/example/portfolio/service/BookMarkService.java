package com.example.portfolio.service;

import com.example.portfolio.entity.BookEntity;

import java.util.Optional;

public interface BookMarkService {

    // 북마크 추가/취소 처리 메소드
    boolean toggleBookmark(String userId, Long bookId);
    // 북마크가 가장 많이 된 책을 찾는 메소드 선언
    Optional<BookEntity> findTopBookByBookmarks();

}
