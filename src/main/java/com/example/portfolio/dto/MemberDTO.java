package com.example.portfolio.dto;


import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class MemberDTO {
    private Long idx;
    private String userid;
    private String pwd;
    private String name;
    private String nickname;
    private int age;
    private String phone;
    private String postcode;
    private String address;
    private String detailAddress;
    private String email;
    private LocalDateTime regdate;
    private String role;
}
