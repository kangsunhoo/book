package com.example.portfolio.service;


import com.example.portfolio.dto.MemberDTO;
import com.example.portfolio.entity.MemberEntity;
import com.example.portfolio.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void join_proc(MemberDTO memberDTO) {

        MemberEntity memberEntity = new MemberEntity();

        memberEntity.setUserid(memberDTO.getUserid());
        memberEntity.setPwd(memberDTO.getPwd());
        memberEntity.setName(memberDTO.getName());
        memberEntity.setNickname(memberDTO.getNickname());
        memberEntity.setAge(memberDTO.getAge());
        memberEntity.setPhone(memberDTO.getPhone());
        memberEntity.setPostcode(memberDTO.getPostcode());
        memberEntity.setAddress(memberDTO.getAddress());
        memberEntity.setDetailAddress(memberDTO.getDetailAddress());
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setRole(memberDTO.getRole() != null ? memberDTO.getRole() : "USER");  // 기본 역할은 USER로 설정


        memberRepository.save(memberEntity);
    }

    //아이디 중복 체크
    public boolean IDcheck(String userid) {
        try {
            return !memberRepository.findByUserid(userid).isEmpty();
        } catch (Exception e) {
            // 로그를 기록하고 false를 반환
            e.printStackTrace(); // 예외 로그 출력
            return false;
        }
    }

    public Page<MemberEntity> member(int page, String keyword) {
        Sort sort = Sort.by(Sort.Direction.DESC, "idx");
        Pageable pageable = PageRequest.of(page, 2, sort);
        Page<MemberEntity> all;

        // keyword가 비어있지 않은 경우에만 검색
        if (keyword.isEmpty()) {
            all = memberRepository.findAll(pageable);
        } else {
            all = memberRepository.findByNameContaining(keyword, pageable);
        }
        all.forEach(member -> System.out.println(member.toString()));
        return all;
    }

    @Transactional
    public void setRoleToAdmin(String userid) {
        // 로그 추가: 메서드 진입
        System.out.println("Entering setRoleToAdmin for user: " + userid);

        MemberEntity member = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        member.setRole("관리자"); // "ADMIN" 대신 "관리자"로 변경
        memberRepository.save(member);

        // 업데이트 후 다시 조회
        MemberEntity updatedMember = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        // 역할이 변경된 후 콘솔에 출력
        System.out.println("Setting role for user " + userid + " to 관리자. Current role: " + updatedMember.getRole());
    }

    public void del(Long idx) {
        MemberEntity member = memberRepository.getReferenceById(idx);
        memberRepository.delete(member);
    }

    public MemberEntity modify(Long idx) {
        if (idx != null && idx != 0) {
            return memberRepository.getReferenceById(idx);
        }
        return null;
    }


    public void modifyMember(MemberDTO memberDTO) {
        MemberEntity memberEntity = memberRepository.getReferenceById(memberDTO.getIdx());

        memberEntity.setUserid(memberDTO.getUserid());
        memberEntity.setPwd(memberDTO.getPwd());
        memberEntity.setName(memberDTO.getName());
        memberEntity.setNickname(memberDTO.getNickname());
        memberEntity.setAge(memberDTO.getAge());
        memberEntity.setPhone(memberDTO.getPhone());
        memberEntity.setPostcode(memberDTO.getPostcode());
        memberEntity.setAddress(memberDTO.getAddress());
        memberEntity.setDetailAddress(memberDTO.getDetailAddress());
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setRole(memberDTO.getRole() != null ? memberDTO.getRole() : "USER");  // 기본 역할은 USER로 설정

        memberRepository.save(memberEntity);
    }




    public MemberEntity login_proc( String userid, String pwd) {

        Optional<MemberEntity> members = memberRepository.findByUserid(userid);

        if (members.isEmpty()) { //isEmpty() 비어있다면 true라는 뜻
            return null;
        }

        // 중복된 사용자가 있을 때 추가 처리 가능
        MemberEntity member = members.get();  // 첫 번째 사용자를 선택 (혹은 추가 로직으로 선택)

        if (member.getPwd().equals(pwd)) {
            return member;
        } else {
            return null;
        }
    }
}










