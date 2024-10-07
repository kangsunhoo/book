package com.example.portfolio.controller;

import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.dto.CategoryDTO;
import com.example.portfolio.dto.ReviewDTO;
import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.service.BookService;
import com.example.portfolio.service.CategoryService;
import com.example.portfolio.service.ReviewService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Book;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class BookController {
    private final CategoryService categoryService;
    private final BookService bookService;
    private final ReviewService reviewService; // ReviewService 추가



    @GetMapping("/book_upload")
    public String showBookUploadForm(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategories(); // DTO 목록 가져오기
        model.addAttribute("categories", categories); // 모델에 추가
        return "book_upload"; // 책 등록 페이지로 이동
    }

    @PostMapping("/book_upload_proc")
    public String bookUploadProc(
            @RequestParam("sbookFile") MultipartFile sbookFile,
            @RequestParam("bookTitle") String bookTitle,
            @RequestParam("bookAuthor") String bookAuthor,
            @RequestParam("bookCompany") String bookCompany,
            @RequestParam("categoryid") Long categoryId,
            @RequestParam("bookContent") String bookContent) throws IOException {

        // BookDTO 객체 생성 및 값 설정
        BookDTO bookDTO = new BookDTO();
        bookDTO.setSbookFile(sbookFile.getOriginalFilename()); // 원본 파일명 설정
        bookDTO.setBookTitle(bookTitle);
        bookDTO.setBookAuthor(bookAuthor);
        bookDTO.setBookCompany(bookCompany);
        bookDTO.setCategoryId(categoryId);
        bookDTO.setBookContent(bookContent);

        // 서버에 저장할 파일 이름 생성
        String uniqueFileName = UUID.randomUUID().toString() + "_" + sbookFile.getOriginalFilename();
        bookDTO.setObookFile(uniqueFileName); // 서버에 저장될 파일 이름

        // BookService를 통해 책 등록 처리
        bookService.saveBook(bookDTO, sbookFile); // 기존 메서드 사용

        return "redirect:/book_list"; // 책 등록 후 홈으로 리다이렉트
    }

    @GetMapping("/book_list")
    public String bookList(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                           HttpServletResponse response,  HttpSession session) {

        //캐시 비활성화
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // Proxies

        Page<BookEntity> allBooks = bookService.getBooks(page, keyword);
        String loginUserId = (String) session.getAttribute("userid");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", allBooks.getTotalPages());
        model.addAttribute("allBooks", allBooks.getContent());
        model.addAttribute("userid", loginUserId);

        return "book_list";
    }

    @GetMapping("/book_detail")
    public String getBookDetail(@RequestParam("bookId") Long bookId, Model model) {
        BookDTO book = bookService.findBookById(bookId);

        // LocalDateTime을 포맷된 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = book.getRegdate().format(formatter);

        model.addAttribute("book", book);
        model.addAttribute("formattedRegdate", formattedDate); // 포맷된 날짜 추가
        return "book_detail";
    }








}