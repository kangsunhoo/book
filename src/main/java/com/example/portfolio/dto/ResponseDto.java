package com.example.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto<T> {
    private int code; // 상태 코드
    private String message; // 메시지
    private T data; // 데이터 (제네릭 타입)

    // 기본 생성자 추가
    public ResponseDto() {
    }
}

