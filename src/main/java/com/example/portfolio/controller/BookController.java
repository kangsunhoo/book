package com.example.portfolio.controller;

import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.dto.CategoryDTO;
import com.example.portfolio.dto.MemberDTO;
import com.example.portfolio.dto.ReviewDTO;
import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.entity.MemberEntity;
import com.example.portfolio.repository.BookRepository;
import com.example.portfolio.service.BookServiceImpl;
import com.example.portfolio.service.CategoryServiceImpl;
import com.example.portfolio.service.ReviewServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class BookController {
    private final CategoryServiceImpl categoryService;
    private final BookServiceImpl bookService;
    private final ReviewServiceImpl reviewService; // ReviewService 추가
    private final BookRepository bookRepository;


    //책 업로드
    @GetMapping("/book_upload")
    public String showBookUploadForm(HttpSession session, Model model) {
        String userid = (String) session.getAttribute("userid");
        model.addAttribute("userid", userid);
        model.addAttribute("role", session.getAttribute("role"));

        List<CategoryDTO> categories = categoryService.getAllCategories(); // DTO 목록 가져오기
        model.addAttribute("categories", categories); // 모델에 추가
        return "book_upload"; // 책 등록 페이지로 이동
    }

    //책 업로드 proc
    @PostMapping("/book_upload_proc")
    public String bookUploadProc(
            @RequestParam("sbookFile") MultipartFile sbookFile,
            @RequestParam("bookTitle") String bookTitle,
            @RequestParam("bookAuthor") String bookAuthor,
            @RequestParam("bookCompany") String bookCompany,
            @RequestParam("categoryid") Long categoryId,
            @RequestParam("bookContent") String bookContent) throws IOException {

        // 서버에 저장할 파일 이름 생성
        String uniqueFileName = UUID.randomUUID().toString() + "_" + sbookFile.getOriginalFilename(); // 파일 이름 생성

        // BookDTO 객체 생성 및 값 설정
        BookDTO bookDTO = new BookDTO();
        bookDTO.setSbookFile(sbookFile.getOriginalFilename()); // 원본 파일명 설정
        bookDTO.setObookFile(uniqueFileName); // 서버에 저장될 파일 이름 설정
        bookDTO.setBookTitle(bookTitle);
        bookDTO.setBookAuthor(bookAuthor);
        bookDTO.setBookCompany(bookCompany);
        bookDTO.setCategoryId(categoryId);
        bookDTO.setBookContent(bookContent);

        // BookService를 통해 책 등록 처리
        bookService.saveBook(bookDTO, sbookFile); // 기존 메서드 사용

        return "redirect:/book_list"; // 책 등록 후 홈으로 리다이렉트
    }


    @GetMapping("/book_list")
    public String bookList(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                           @RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
                           HttpServletResponse response, HttpSession session) {

        // 캐시 비활성화
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Pageable 설정: 최신 등록일 기준 내림차순 정렬
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "regdate"));
        Page<BookEntity> allBooks; // Page 타입으로 설정

        // 검색 타입에 따른 분기 처리
        switch (searchType) {
            case "title":
                // 제목으로 검색
                allBooks = bookService.searchBooksByTitle(pageable, keyword);
                break;
            case "author":
                // 작가로 검색
                allBooks = bookService.searchBooksByAuthor(pageable, keyword);
                break;
            default:
                // 검색어가 없을 경우 전체 목록 반환
                allBooks = bookService.getBooks(pageable, null);
                break;
        }

        // 세션에서 사용자 ID 및 역할 정보 가져오기
        String loginUserId = (String) session.getAttribute("userid");
        String userRole = (String) session.getAttribute("role");

        // 모델에 데이터 추가
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", allBooks.getTotalPages()); // 전체 페이지 수
        model.addAttribute("allBooks", allBooks); // Page 객체 전체를 전달
        model.addAttribute("userid", loginUserId);
        model.addAttribute("role", userRole);

        return "book_list"; // 뷰 이름 반환
    }




    // 책 디테일
    @GetMapping("/book_detail")
    public String bookDetail(@RequestParam("bookId") Long bookId, Model model, HttpSession session) {

        String role = (String) session.getAttribute("role");  // 세션에서 role 가져오기
        model.addAttribute("role", role);  // 역할 정보를 모델에 추가

        System.out.println("Received bookId: " + bookId); // 로그 추가
        BookEntity book = bookService.getBookById(bookId); // BookService에서 책 정보 가져오기
        if (book == null) {
            // 404 페이지로 리다이렉트
            return "error/404"; // 또는 다른 에러 처리 방식
        }


        // 게시판 데이터 가져오기 컨트롤러

        BookDTO previousBook = bookService.getPreviousBook(bookId);
        BookDTO nextBook = bookService.getNextBook(bookId);


        List<ReviewDTO> reviews = reviewService.getReviewsByBookId(bookId); // 리뷰 목록 가져오기
        String loginUserId = (String) session.getAttribute("userid"); // 세션에서 사용자 ID 가져오기

        model.addAttribute("book", book); // 책 정보 모델에 추가
        model.addAttribute("previousBook", previousBook);
        model.addAttribute("nextBook", nextBook);
        model.addAttribute("reviews", reviews); // 리뷰 목록 모델에 추가
        model.addAttribute("userid", loginUserId); // 사용자 ID 모델에 추가

        return "book_detail"; // 뷰 이름 반환
    }
    // 선택된 책 삭제
    @PostMapping("/book/deleteSelected")
    public String deleteSelectedBooks(@RequestParam("selectedIds") List<Long> selectedIds) {
        // 선택된 책 삭제
        bookService.deleteSelectedBooks(selectedIds);
        return "redirect:/book_list";  // 삭제 후 목록 페이지로 리다이렉트
    }



    @GetMapping("/book_modify")
    public String book_modify(@RequestParam(value = "bookId") Long bookId, Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");

        // 관리자만 접근 가능
        if (!"관리자".equals(role)) {
            return "redirect:/access_denied"; // 접근 거부 페이지 또는 다른 페이지로 리다이렉트
        }

        String userid = (String) session.getAttribute("userid");
        model.addAttribute("userid", userid);

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다. bookId: " + bookId));
        model.addAttribute("book", book);

        return "book_modify"; // 수정 페이지로 이동
    }


    @PostMapping("/book_modify_proc")
    public String book_modify_proc(@ModelAttribute BookDTO bookDTO) {
        if (bookDTO.getBookId() == null) {
            throw new IllegalArgumentException("Book ID가 전달되지 않았습니다.");
        }
        bookService.modifybook(bookDTO);
        return "redirect:/book_detail?bookId=" + bookDTO.getBookId(); // 수정 후 상세 페이지로 리다이렉트
    }





}