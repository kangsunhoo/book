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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository; // 카테고리 리포지토리 주입

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository; // 생성자 주입
    }

    @Override
    public CategoryEntity getCategoryById(Long categoryId) {
        // 카테고리 ID로 카테고리 조회
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found")); // 예외 처리
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        // 모든 카테고리 조회
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO) // Entity를 DTO로 변환
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        // 카테고리 추가
        CategoryEntity category = convertToEntity(categoryDTO); // DTO를 Entity로 변환
        CategoryEntity savedCategory = categoryRepository.save(category); // Entity 저장
        return convertToDTO(savedCategory); // 저장된 Entity를 DTO로 변환하여 반환
    }

    // DTO를 Entity로 변환
    private CategoryEntity convertToEntity(CategoryDTO categoryDTO) {
        CategoryEntity category = new CategoryEntity();
        category.setCategoryName(categoryDTO.getCategoryName()); // 카테고리 이름 설정
        return category;
    }

    // Entity를 DTO로 변환
    private CategoryDTO convertToDTO(CategoryEntity categoryEntity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(categoryEntity.getCategoryId()); // 카테고리 ID 설정
        dto.setCategoryName(categoryEntity.getCategoryName()); // 카테고리 이름 설정
        return dto;
    }
}
