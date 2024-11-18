package com.example.portfolio.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@AllArgsConstructor
public class MyPageController {

    @GetMapping("/mypage")
    public String mypage() {

        return "mypage";
    }
}
