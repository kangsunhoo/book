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

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void join_proc(MemberDTO memberDTO) {
        // 회원 가입 처리
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
        memberEntity.setRole(memberDTO.getRole() != null ? memberDTO.getRole() : "USER"); // 기본 역할은 USER로 설정

        memberRepository.save(memberEntity);
    }

    @Override
    public boolean IDcheck(String userid) {
        // 아이디 중복 체크
        try {
            return !memberRepository.findByUserid(userid).isEmpty();
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return false;
        }
    }

    @Override
    public Page<MemberEntity> member(Pageable pageable, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            // 전체 회원 조회 - pageable 사용
            return memberRepository.findAll(pageable);
        } else {
            // 키워드로 회원 조회 - pageable 사용
            return memberRepository.findByNameContaining(keyword, pageable);
        }
    }

    @Override
    @Transactional
    public void setRoleToAdmin(String userid) {
        // 역할을 관리자(ADMIN)로 변경
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

    @Override
    public void del(Long idx) {
        // 회원 삭제
        MemberEntity member = memberRepository.getReferenceById(idx);
        memberRepository.delete(member);
    }

    @Override
    public MemberEntity modify(Long idx) {
        // 회원 수정 정보 조회
        if (idx != null && idx != 0) {
            return memberRepository.getReferenceById(idx);
        }
        return null;
    }

    @Override
    public void modifyMember(MemberDTO memberDTO) {
        // 회원 정보 수정
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
        memberEntity.setRole(memberDTO.getRole() != null ? memberDTO.getRole() : "USER"); // 기본 역할은 USER로 설정

        memberRepository.save(memberEntity);
    }

    @Override
    public MemberEntity login_proc(String userid, String pwd) {
        // 로그인 처리
        Optional<MemberEntity> members = memberRepository.findByUserid(userid);

        if (members.isEmpty()) { // isEmpty() 비어있다면 true라는 뜻
            return null;
        }

        // 중복된 사용자가 있을 때 추가 처리 가능
        MemberEntity member = members.get(); // 첫 번째 사용자를 선택 (혹은 추가 로직으로 선택)

        if (member.getPwd().equals(pwd)) {
            return member;
        } else {
            return null;
        }
    }

    @Override
    public MemberEntity findById(String userId) {
        return memberRepository.findByUserid(userId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }
}
