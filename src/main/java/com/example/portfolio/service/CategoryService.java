package com.example.portfolio.service;

import com.example.portfolio.dto.CategoryDTO;
import com.example.portfolio.entity.CategoryEntity;
import com.example.portfolio.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found")); // 예외 처리
    }

    // 모든 카테고리 조회
    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 카테고리 추가
    @Transactional
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        CategoryEntity category = convertToEntity(categoryDTO);
        CategoryEntity savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    // DTO를 Entity로 변환
    private CategoryEntity convertToEntity(CategoryDTO categoryDTO) {
        CategoryEntity category = new CategoryEntity();
        category.setCategoryName(categoryDTO.getCategoryName());
        return category;
    }

    // Entity를 DTO로 변환
    private CategoryDTO convertToDTO(CategoryEntity categoryEntity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(categoryEntity.getCategoryId());
        dto.setCategoryName(categoryEntity.getCategoryName());
        return dto;
    }
}