package com.example.portfolio.repository;

import com.example.portfolio.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 작성
}
