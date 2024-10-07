package com.example.portfolio.controller;

import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.dto.CategoryDTO;
import com.example.portfolio.dto.MemberDTO;
import com.example.portfolio.dto.ResponseDto;
import com.example.portfolio.entity.CategoryEntity;
import com.example.portfolio.entity.MemberEntity;
import com.example.portfolio.repository.MemberRepository;
import com.example.portfolio.service.BookService;
import com.example.portfolio.service.CategoryService;
import com.example.portfolio.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CategoryService categoryService; // 추가된 서비스
    private final BookService bookService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String userid = (String) session.getAttribute("userid");
        model.addAttribute("userid", userid);
        model.addAttribute("role", session.getAttribute("role"));

        // 모든 카테고리 조회
        List<CategoryDTO> categories = categoryService.getAllCategories();

        // 각 카테고리에 속한 책 목록 조회
        Map<Long, List<BookDTO>> booksByCategory = categories.stream()
                .collect(Collectors.toMap(
                        CategoryDTO::getCategoryId,
                        category -> bookService.getBooksByCategoryId(category.getCategoryId())
                ));

        model.addAttribute("categories", categories);
        model.addAttribute("booksByCategory", booksByCategory);

        return "home"; // home.html을 반환
    }

    @GetMapping("/join")
    public String join(){

        return "join";
    }
    @PostMapping("/join_proc")
    public String join_proc(MemberDTO memberDTO, @RequestParam("fullEmail") String fullEmail, Model model){

        memberDTO.setEmail(fullEmail);
        memberService.join_proc(memberDTO);
        return "redirect:/login"; // 변경

    }

    @GetMapping("/IDCheck")
    public @ResponseBody ResponseDto<?> check(@RequestParam("userid") String userid) {
        System.out.println("Received userid: " + userid); // 추가된 로그

        if (userid == null || userid.isEmpty()) {
            return new ResponseDto<>(-1, "아이디를 입력해주세요.", null);
        }

        boolean isDuplicate = memberService.IDcheck(userid);
        if (isDuplicate) {
            return new ResponseDto<>(1, "동일한 아이디가 존재합니다.", true); // 이미 사용 중인 아이디
        } else {
            return new ResponseDto<>(1, "사용 가능한 아이디입니다.", false); // 사용 가능한 아이디
        }
    }


    @GetMapping("/member")
    public String member(Model model, HttpSession session,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {

        // 세션에서 역할 확인
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("관리자")) {
            return "redirect:/home"; // 관리자가 아니면 홈으로 리다이렉트
        }

        Page<MemberEntity> all = memberService.member(page, keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", all.getTotalPages());
        model.addAttribute("all", all);

        return "member";
    }


    @PostMapping("/set-admin/{userid}")
    public ResponseEntity<?> setAdmin(@PathVariable String userid) {
        // 요청 수신 로그 추가
        System.out.println("setAdmin called for user: " + userid);

        try {
            memberService.setRoleToAdmin(userid);
            // 역할 업데이트 성공 로그 추가
            System.out.println("Member role updated to 관리자 for user: " + userid);
            return ResponseEntity.ok("Member role updated to 관리자");
        } catch (Exception e) {
            // 오류 발생 시 로그 추가
            System.err.println("Error updating member role for user: " + userid + " - " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to update member role");
        }
    }


    @GetMapping("/login")
    public String login(){

        return "login";
    }

    @PostMapping("/login_proc")
    public String login_proc(@RequestParam(value = "userid") String userid,
                             @RequestParam(value = "pwd") String pwd,
                             Model model, HttpSession session) {

        MemberEntity member = memberService.login_proc(userid, pwd);

        if (member == null) { // 사용자 없음
            model.addAttribute("error", "User not found.");
            return "login"; // 로그인 페이지로 돌아가기
        }

        // 세션에 사용자 정보와 역할 저장
        session.setAttribute("userid", userid);
        session.setAttribute("member", member);
        session.setAttribute("role", member.getRole()); // 역할 저장

        return "redirect:/home"; // 홈으로 리다이렉트
    }

    @GetMapping("/logout")
    private String logout(HttpSession session){
        session.invalidate();
        return "redirect:/home";
    }

    @GetMapping("/del")
    public String del(@RequestParam(value = "idx")Long idx){

        memberService.del(idx);

        return "redirect:/member";
    }

    @GetMapping("/modify")
    public String modify(@RequestParam(value = "idx")Long idx, Model model){

        MemberEntity member= new MemberEntity();

        if(idx != 0){
            member= memberRepository.getReferenceById(idx);
        }
        model.addAttribute("member",member);
        return "modify";
    }


    @PostMapping("/modify_proc")
    public String modify_proc(@ModelAttribute MemberDTO memberDTO,
                              @RequestParam("fullEmail") String fullEmail) {
        // 다른 유효성 검사 코드...

        // 이메일을 memberDTO에 저장
        memberDTO.setEmail(fullEmail);

        memberService.modifyMember(memberDTO);
        return "redirect:/member"; // 수정 후 리다이렉트
    }












}
