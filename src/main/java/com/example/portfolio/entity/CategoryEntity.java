package com.example.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@Getter
@ToString
@NoArgsConstructor
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid", unique = true, nullable = false)
    private Long categoryId;         // 카테고리 ID

    @Column(name = "CATEGORYNAME", nullable = false, length = 50)
    private String categoryName;     // 카테고리 이름

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookEntity> books; // 해당 카테고리에 속한 책들

    // 생성자, getter, setter는 Lombok이 자동으로 생성
}

