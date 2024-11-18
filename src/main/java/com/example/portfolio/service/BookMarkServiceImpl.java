package com.example.portfolio.service;

import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.entity.BookMarkEntity;
import com.example.portfolio.entity.MemberEntity;
import com.example.portfolio.repository.BookMarkRepository;
import com.example.portfolio.repository.BookRepository;
import com.example.portfolio.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookMarkServiceImpl implements BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    @Override
    public boolean toggleBookmark(String userid, Long bookId) {
        Optional<MemberEntity> userOpt = memberRepository.findByUserid(userid);
        Optional<BookEntity> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isPresent() && bookOpt.isPresent()) {
            MemberEntity user = userOpt.get();
            BookEntity book = bookOpt.get();

            // 이미 북마크가 존재하는지 확인
            Optional<BookMarkEntity> existingBookmark = bookMarkRepository.findByUserAndBook(user, book);

            if (existingBookmark.isPresent()) {
                // 북마크가 존재하면 삭제
                bookMarkRepository.delete(existingBookmark.get());
                return false; // 북마크 취소됨
            } else {
                // 북마크가 없으면 추가
                BookMarkEntity newBookmark = new BookMarkEntity();
                newBookmark.setUser(user);
                newBookmark.setBook(book);
                bookMarkRepository.save(newBookmark);
                return true; // 북마크 추가됨
            }
        }

        // 유효하지 않은 사용자나 책인 경우 로그 추가
        System.out.println("Invalid user or book: " + userid + ", " + bookId);
        return false; // 유효하지 않은 사용자나 책인 경우
    }


    @Override
    public Optional<BookEntity> findTopBookByBookmarks() {
        Pageable pageable = PageRequest.of(0, 1); // 0번째 페이지에서 하나의 책만 가져옴
        Page<BookEntity> topBooksPage = bookMarkRepository.findTopBookByBookmarks(pageable);
        return topBooksPage.hasContent() ? Optional.of(topBooksPage.getContent().get(0)) : Optional.empty();
    }


}