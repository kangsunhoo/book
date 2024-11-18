package com.example.portfolio.service;

import com.example.portfolio.dto.CommentDTO;
import java.util.List;

public interface CommentService {

    // 댓글 추가
    void addComment(CommentDTO commentDTO);

    // 특정 게시판의 댓글 조회
    List<CommentDTO> getCommentsByBoardId(Long boardIdx);

    // 특정 사용자의 댓글 조회 (옵션)
    List<CommentDTO> getCommentsByUserId(String userId);

    // 댓글 삭제
    void deleteComment(Long idx);

    // 댓글 수정
    // Service Interface
    void updateComment(Long idx, CommentDTO.Request commentRequestDTO);


    // 댓글 ID로 댓글 조회
    CommentDTO getCommentById(Long idx);

    List<CommentDTO> getCommentsByBoardIdx(Long boardIdx);
}