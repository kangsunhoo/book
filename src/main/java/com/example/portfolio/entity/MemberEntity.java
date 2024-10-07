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

    @Column(name = "userid")
    private  String userid;

    @Column(name = "pwd")
    private  String pwd;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "age")
    private int age;

    @Column(name = "phone")
    private String phone;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "address")
    private String address;

    @Column(name = "DETAILADDRESS")
    private String detailAddress;

    @Column(name = "email")
    private String email;

    @Column(name = "regdate")
    private LocalDateTime regdate = LocalDateTime.now();

    @Column(name = "role")
    private String role;
}
