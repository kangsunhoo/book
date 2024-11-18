package com.example.portfolio.service;

import com.example.portfolio.dto.MemberDTO;
import com.example.portfolio.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    // 회원 가입 처리
    void join_proc(MemberDTO memberDTO);

    // 아이디 중복 체크
    boolean IDcheck(String userid);

    // 회원 목록 조회
    Page<MemberEntity> member(Pageable pageable, String keyword);

    // 역할을 관리자(ADMIN)로 변경
    void setRoleToAdmin(String userid);

    // 회원 삭제
    void del(Long idx);

    // 회원 수정 정보 조회
    MemberEntity modify(Long idx);

    // 회원 정보 수정
    void modifyMember(MemberDTO memberDTO);

    // 로그인 처리
    MemberEntity login_proc(String userid, String pwd);

    MemberEntity findById(String userId);

}

