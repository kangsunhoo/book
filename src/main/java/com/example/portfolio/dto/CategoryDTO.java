package com.example.portfolio.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategoryDTO {
    private Long categoryId;       // 카테고리 ID
    private String categoryName;   // 카테고리 이름
}
