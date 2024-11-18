package com.example.portfolio.service;

import com.example.portfolio.dto.CategoryDTO;
import com.example.portfolio.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
    // 카테고리 ID로 카테고리 조회
    CategoryEntity getCategoryById(Long categoryId);

    // 모든 카테고리 조회
    List<CategoryDTO> getAllCategories();

    // 카테고리 추가
    CategoryDTO addCategory(CategoryDTO categoryDTO);
}

