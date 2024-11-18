package com.example.portfolio.service;

import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.dto.MemberDTO;
import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.entity.CategoryEntity;
import com.example.portfolio.entity.MemberEntity;
import com.example.portfolio.repository.BookMarkRepository;
import com.example.portfolio.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository; // BookRepository 주입
    private final BookMarkRepository bookMarkRepository; // BookMarkRepository 주입
    private final CategoryServiceImpl categoryService; // CategoryService 주입

    // 생성자 주입 (권장 방식)
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMarkRepository bookMarkRepository, CategoryServiceImpl categoryService) {
        this.bookRepository = bookRepository;
        this.bookMarkRepository = bookMarkRepository;
        this.categoryService = categoryService;
    }

    @Override
    public BookDTO findBookById(Long bookId) {
        // bookId로 책 조회
        Optional<BookEntity> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            return convertToDTO(optionalBook.get()); // DTO로 변환하여 반환
        } else {
            throw new RuntimeException("책을 찾을 수 없습니다. bookId: " + bookId);
        }
    }

    @Override
    public BookEntity getBookById(Long bookId) {
        // bookId로 책 조회 (Entity로 반환)
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다. bookId: " + bookId));
    }

    // BookEntity를 BookDTO로 변환하는 메서드
    private BookDTO convertToDTO(BookEntity book) {
        BookDTO dto = new BookDTO();
        dto.setBookId(book.getBookId());
        dto.setSbookFile(book.getSbookFile());
        dto.setObookFile(book.getObookFile());
        dto.setBookTitle(book.getBookTitle());
        dto.setBookContent(book.getBookContent());
        dto.setBookAuthor(book.getBookAuthor());
        dto.setBookCompany(book.getBookCompany());
        dto.setCategoryId(book.getCategory().getCategoryId());
        dto.setCategoryName(book.getCategory().getCategoryName());
        dto.setRegdate(book.getRegdate()); // 등록 날짜 설정
        return dto;
    }

    @Override
    public List<BookDTO> getBooksByCategoryId(Long categoryId) {
        // 카테고리 ID로 책을 등록일 기준 내림차순으로 조회
        List<BookEntity> books = bookRepository.findByCategory_CategoryIdOrderByRegdateDesc(categoryId);
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public void saveBook(BookDTO bookDTO, MultipartFile sbookFile) {
        BookEntity bookEntity = new BookEntity(); // 새로운 BookEntity 생성
        String uploadDir = "src/main/resources/static/uploads/"; // 파일 저장 디렉토리 설정
        String uniqueFileName = UUID.randomUUID().toString() + "_" + sbookFile.getOriginalFilename(); // 파일 이름 생성

        try {
            // 파일 저장
            Files.copy(sbookFile.getInputStream(), Paths.get(uploadDir + uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace(); // 오류 출력
        }

        bookEntity.setSbookFile(sbookFile.getOriginalFilename()); // 원본 파일명 저장
        bookEntity.setObookFile(uniqueFileName); // 서버에 저장할 파일명 저장

        // BookEntity에 다른 정보 설정
        bookEntity.setBookTitle(bookDTO.getBookTitle());
        bookEntity.setBookContent(bookDTO.getBookContent());
        bookEntity.setBookAuthor(bookDTO.getBookAuthor());
        bookEntity.setBookCompany(bookDTO.getBookCompany());

        // 카테고리 설정
        CategoryEntity category = categoryService.getCategoryById(bookDTO.getCategoryId());
        bookEntity.setCategory(category);

        // 등록 날짜 설정
        bookEntity.setRegdate(java.time.LocalDateTime.now());

        // BookEntity 저장
        bookRepository.save(bookEntity);
    }




    @Override
    public List<BookDTO> getRecentBooks(int limit) {
        // 최근 등록된 책 목록 조회
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "regdate"));
        Page<BookEntity> recentBooksPage = bookRepository.findAll(pageable);
        List<BookEntity> recentBooks = recentBooksPage.getContent();
        return recentBooks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public List<BookEntity> getRecentRomanceBooks(int limit) {
        // "로맨스" 장르로 필터링하고 최신 limit권만 가져오는 로직
        Pageable pageable = PageRequest.of(0, limit);
        return bookRepository.findTop3ByCategory_CategoryNameOrderByRegdateDesc("로맨스", pageable);
    }
    @Override
    public List<BookEntity> getRecentMistaryBooks(int limit) {
        // "추리" 장르로 필터링하고 최신 limit권만 가져오는 로직
        Pageable pageable = PageRequest.of(0, limit);
        return bookRepository.findTop3ByCategory_CategoryNameOrderByRegdateDesc("추리", pageable);
    }

    @Override
    public List<BookEntity> getRecentFantasyBooks(int limit) {
        // "추리" 장르로 필터링하고 최신 limit권만 가져오는 로직
        Pageable pageable = PageRequest.of(0, limit);
        return bookRepository.findTop3ByCategory_CategoryNameOrderByRegdateDesc("판타지", pageable);
    }


    @Override
    public BookEntity findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found")); // 책이 없을 경우 예외 처리
    }

    @Override
    public void deleteSelectedBooks(List<Long> selectedIds) {
        // 선택된 책들을 모두 삭제
        bookRepository.deleteAllById(selectedIds);
    }

    @Override
    public BookEntity book_modify(Long bookId) {
        // 회원 수정 정보 조회
        if (bookId != null && bookId != 0) {
            return bookRepository.getReferenceById(bookId);
        }
        return null;
    }


    public void modifybook(BookDTO bookDTO) {
        // 회원 정보 수정
        BookEntity bookEntity = bookRepository.getReferenceById(bookDTO.getBookId());

        bookEntity.setSbookFile(bookDTO.getSbookFile());
        bookEntity.setBookTitle(bookDTO.getBookTitle());
        bookEntity.setBookContent(bookDTO.getBookContent());
        bookEntity.setBookAuthor(bookDTO.getBookAuthor());
        bookEntity.setBookCompany(bookDTO.getBookCompany());

        bookRepository.save(bookEntity);
    }

    @Override
    public Page<BookEntity> searchBooksByTitle(Pageable pageable, String keyword) {
        // 제목에 해당하는 책 검색
        return bookRepository.findByBookTitleContaining(keyword, pageable);
    }

    @Override
    public Page<BookEntity> searchBooksByAuthor(Pageable pageable, String keyword) {
        // 작가에 해당하는 책 검색
        return bookRepository.findByBookAuthorContaining(keyword, pageable);
    }

    @Override
    public Page<BookEntity> getBooks(Pageable pageable, String keyword) {
        // 키워드가 있을 경우 제목으로 검색하고, 없을 경우 전체 책 목록 반환
        if (keyword != null && !keyword.isEmpty()) {
            return bookRepository.findByBookTitleContaining(keyword, pageable);
        }
        return bookRepository.findAll(pageable);
    }

    @Override
    public BookDTO getPreviousBook(Long bookId) {
        return bookRepository.findTopByBookIdLessThanOrderByBookIdDesc(bookId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public BookDTO getNextBook(Long bookId) {
        return bookRepository.findTopByBookIdGreaterThanOrderByBookIdAsc(bookId)
                .map(this::convertToDTO)
                .orElse(null);
    }





}
