package com.example.portfolio.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.portfolio.dto.CategoryDTO;
import com.example.portfolio.service.CategoryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class CategoryController {

    private CategoryServiceImpl categoryService;

    // 카테고리 추가 폼 페이지 표시
    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model, HttpSession session) {

        String userid = (String) session.getAttribute("userid");
        model.addAttribute("userid", userid);
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "category/add"; // templates/category/add.html
    }

    // 카테고리 추가 처리
    @PostMapping("/categories/add")
    public String addCategory(CategoryDTO categoryDTO, Model model) {
        try {
            categoryService.addCategory(categoryDTO);
            return "redirect:/categories"; // 카테고리 목록 페이지로 리다이렉트
        } catch (Exception e) {
            model.addAttribute("errorMessage", "카테고리 추가에 실패했습니다.");
            return "category/add"; // 에러 발생 시 다시 추가 폼 페이지로 이동
        }
    }

    // 카테고리 목록 페이지 표시
    @GetMapping("/categories")
    public String listCategories(Model model, HttpSession session) {

        String userid = (String) session.getAttribute("userid");
        model.addAttribute("userid", userid);
        model.addAttribute("role", session.getAttribute("role"));

        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/list"; // templates/category/list.html
    }
}
