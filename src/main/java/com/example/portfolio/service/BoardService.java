package com.example.portfolio.service;

import com.example.portfolio.dto.BoardDTO;
import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.entity.BoardEntity;
import com.example.portfolio.entity.BookEntity;

import java.util.List;

public interface BoardService {
    void saveBoard(BoardDTO boardDTO);  // 게시글 저장
    List<BoardDTO> getAllBoards();  // 모든 게시글 가져오기
    BoardDTO getBoardById(Long idx);  // 특정 게시글 가져오기
    void deleteSelectedBoards(List<Long> selectedIds);  // 여러 게시글 삭제
    void toggleLikes(Long idx, String userid);  // 좋아요 토글 메서드 추가

    // 회원 수정 정보 조회
    BoardEntity board_modify(Long idx);

    // 회원 정보 수정
    void modifyboard(BoardDTO boardDTO);

    BoardDTO getPreviousBoard(Long idx);
    BoardDTO getNextBoard(Long idx);
}

