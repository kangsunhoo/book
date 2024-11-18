package com.example.portfolio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@ToString
@NoArgsConstructor
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idx", unique = true, nullable = false)
    private Long idx;

    @Column(name = "userid", unique = true, nullable = false)
    private String userid; // 사용자 ID

    @Column(name = "pwd", nullable = false)
    private  String pwd;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "postcode" ,nullable = false)
    private String postcode;

    @Column(name = "address",nullable = false)
    private String address;

    @Column(name = "DETAILADDRESS",nullable = false)
    private String detailAddress;

    @Column(name = "email",nullable = false )
    private String email;

    @Column(name = "regdate",nullable = false)
    private LocalDateTime regdate = LocalDateTime.now();

    @Column(name = "role")
    private String role;
}
