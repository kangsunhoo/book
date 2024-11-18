package com.example.portfolio.controller;

import com.example.portfolio.service.BookMarkService;
import com.example.portfolio.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/bookmark")
public class BookMarkController {

    @Autowired
    private BookMarkService bookMarkService;
    private final BookService bookService;

    // 북마크 추가 또는 취소
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleBookmark(@RequestParam("bookId") Long bookId, HttpSession session) {
        String userId = (String) session.getAttribute("userid");

        // 세션에서 사용자 정보가 없을 때 처리
        if (userId == null) {
            return ResponseEntity.status(403).body("로그인이 필요합니다.");
        }

        try {
            // 북마크 추가 또는 취소 처리
            boolean isBookmarked = bookMarkService.toggleBookmark(userId, bookId);

            if (isBookmarked) {
                return ResponseEntity.ok("북마크가 추가되었습니다.");
            } else {
                return ResponseEntity.ok("북마크가 취소되었습니다.");
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("북마크 처리 중 오류가 발생했습니다.");
        }
    }



}
