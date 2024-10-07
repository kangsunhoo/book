package com.example.portfolio.service;

import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.entity.CategoryEntity;
import com.example.portfolio.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    // 생성자 주입 (권장 방식)
    @Autowired
    public BookService(BookRepository bookRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }

    // 기존 메서드들...

    /**
     * 특정 bookId에 해당하는 책을 조회하여 BookDTO로 반환
     *
     * @param bookId 조회할 책의 ID
     * @return BookDTO 객체
     * @throws RuntimeException 책을 찾을 수 없을 경우 예외 발생
     */
    public BookDTO findBookById(Long bookId) {
        Optional<BookEntity> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isPresent()) {
            return convertToDTO(optionalBook.get());
        } else {
            throw new RuntimeException("책을 찾을 수 없습니다. bookId: " + bookId);
        }
    }

    /**
     * BookEntity를 BookDTO로 변환
     *
     * @param book BookEntity 객체
     * @return BookDTO 객체
     */
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
        dto.setCategoryName(book.getCategory().getCategoryName()); // 카테고리 이름 설정
        dto.setRegdate(book.getRegdate()); // 등록 날짜 설정 (BookDTO에 필드 추가 필요)
        return dto;
    }

    public List<BookDTO> getBooksByCategoryId(Long categoryId) {
        List<BookEntity> books = bookRepository.findByCategory_CategoryId(categoryId);
        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void saveBook(BookDTO bookDTO, MultipartFile sbookFile) {
        BookEntity bookEntity = new BookEntity();

        // 서버에 저장할 파일 이름 생성
        String uniqueFileName = UUID.randomUUID().toString() + "_" + sbookFile.getOriginalFilename();

        // 파일 저장 경로
        String filePath = "src/main/resources/static/uploads/";

        try {
            // 이미지 파일 저장
            Files.copy(sbookFile.getInputStream(), Paths.get(filePath + uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bookEntity.setSbookFile(sbookFile.getOriginalFilename()); // 작은 파일명 저장
        bookEntity.setObookFile(uniqueFileName); // 이미지 파일명 저장

        // BookEntity에 다른 정보 설정
        bookEntity.setBookTitle(bookDTO.getBookTitle());
        bookEntity.setBookContent(bookDTO.getBookContent());
        bookEntity.setBookAuthor(bookDTO.getBookAuthor());
        bookEntity.setBookCompany(bookDTO.getBookCompany());

        // CategoryEntity 설정
        CategoryEntity category = categoryService.getCategoryById(bookDTO.getCategoryId());
        bookEntity.setCategory(category);

        // 등록 날짜 설정
        bookEntity.setRegdate(java.time.LocalDateTime.now());

        // BookEntity 저장
        bookRepository.save(bookEntity);
    }

    private String saveFile(MultipartFile file) {
        String filePath = "src/main/resources/static/uploads/"; // 예: "/uploads/"
        String filename = file.getOriginalFilename();
        Path path = Paths.get(filePath + filename);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath + filename; // 최종 경로 반환
    }

    // bookUploadProc 메서드에서 호출
    public Page<BookEntity> getBooks(int page, String keyword) {
        Pageable pageable = PageRequest.of(page, 5); // 한 페이지에 5개의 책
        if (keyword != null && !keyword.isEmpty()) {
            return bookRepository.findByBookTitleContaining(keyword, pageable); // 제목으로 검색
        }
        return bookRepository.findAll(pageable); // 모든 책 반환
    }
}
