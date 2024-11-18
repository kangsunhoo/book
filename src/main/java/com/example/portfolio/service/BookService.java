package com.example.portfolio.service;

import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.dto.MemberDTO;
import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BookService {
    // 특정 bookId에 해당하는 책을 조회하여 BookDTO로 반환
    BookDTO findBookById(Long bookId);

    // 특정 bookId에 해당하는 책을 조회하여 BookEntity로 반환
    BookEntity getBookById(Long bookId);

    // 특정 카테고리 ID에 해당하는 책 목록을 조회
    List<BookDTO> getBooksByCategoryId(Long categoryId);

    // 책 정보를 저장하는 메서드 (파일 포함)
    void saveBook(BookDTO bookDTO, MultipartFile sbookFile);

        Page<BookEntity> searchBooksByTitle(Pageable pageable, String keyword);
        Page<BookEntity> searchBooksByAuthor(Pageable pageable, String keyword);
        Page<BookEntity> getBooks(Pageable pageable, String keyword);

    // 최근 등록된 책 목록을 조회 (제한 수)
    List<BookDTO> getRecentBooks(int limit);

    // 로맨스 장르의 신작 도서 3권 조회
    List<BookEntity> getRecentRomanceBooks(int limit);

    // 추리 장르의 신작 도서 3권 조회
    List<BookEntity> getRecentMistaryBooks(int limit);

    // 추리 장르의 신작 도서 3권 조회
    List<BookEntity> getRecentFantasyBooks(int limit);

    BookEntity findById(Long bookId);

    // 기존 메서드 외에 선택된 책 삭제 기능 추가
    void deleteSelectedBooks(List<Long> selectedIds);

    // 회원 수정 정보 조회
    BookEntity book_modify(Long bookId);

    // 회원 정보 수정
    void modifybook(BookDTO bookDTO);

    BookDTO getPreviousBook(Long bookId);
    BookDTO getNextBook(Long bookId);

}

