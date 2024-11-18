package com.example.portfolio.service;

import com.example.portfolio.dto.CommentDTO;
import com.example.portfolio.entity.CommentEntity;
import com.example.portfolio.entity.MemberEntity;
import com.example.portfolio.entity.BoardEntity;
import com.example.portfolio.repository.CommentRepository;
import com.example.portfolio.repository.MemberRepository;
import com.example.portfolio.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public void addComment(CommentDTO commentDTO) {
        MemberEntity member = memberRepository.findByUserid(commentDTO.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자(ID)가 존재하지 않습니다: " + commentDTO.getUserid()));

        BoardEntity board = boardRepository.findById(commentDTO.getBoardIdx())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글(ID)이 존재하지 않습니다: " + commentDTO.getBoardIdx()));


        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setMember(member);
        commentEntity.setBoard(board);
        commentEntity.setCommentText(commentDTO.getCommentText());
        commentEntity.setNickname(commentDTO.getNickname());

        commentRepository.save(commentEntity);
    }

    @Override
    public List<CommentDTO> getCommentsByBoardId(Long boardIdx) {
        List<CommentEntity> comments = commentRepository.findByBoardIdx(boardIdx);
        return comments.stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            dto.setIdx(comment.getIdx());
            dto.setNickname(comment.getNickname());
            dto.setCommentText(comment.getCommentText());
            dto.setRegdate(comment.getRegdate());
            dto.setUserid(comment.getMember().getUserid());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByUserId(String userId) {
        List<CommentEntity> comments = commentRepository.findByMember_Userid(userId);
        return comments.stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            dto.setIdx(comment.getIdx());
            dto.setNickname(comment.getNickname());
            dto.setCommentText(comment.getCommentText());
            dto.setRegdate(comment.getRegdate());
            dto.setUserid(comment.getMember().getUserid());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long idx) {
        // 댓글이 존재하는지 확인
        if (!commentRepository.existsById(idx)) {
            throw new EntityNotFoundException("Comment not found with id: " + idx);
        }
        // 댓글 삭제
        commentRepository.deleteById(idx);
    }

    // Service Implementation
    @Override
    public void updateComment(Long idx, CommentDTO.Request commentRequestDTO) {
        CommentEntity commentEntity = commentRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        commentEntity.update(commentRequestDTO.getCommentText());
        commentRepository.save(commentEntity);  // 이 줄 추가
    }




    @Override
    public CommentDTO getCommentById(Long idx) {
        CommentEntity comment = commentRepository.findByIdx(idx);
        CommentDTO dto = new CommentDTO();
        dto.setIdx(comment.getIdx());
        dto.setNickname(comment.getNickname());
        dto.setCommentText(comment.getCommentText());
        dto.setRegdate(comment.getRegdate());
        dto.setUserid(comment.getMember().getUserid());
        return dto;
    }

    @Override
    public List<CommentDTO> getCommentsByBoardIdx(Long boardIdx) { // 메소드 이름 수정
        List<CommentEntity> comments = commentRepository.findByBoardIdx(boardIdx);
        return comments.stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            dto.setIdx(comment.getIdx());
            dto.setNickname(comment.getNickname());
            dto.setCommentText(comment.getCommentText());
            dto.setRegdate(comment.getRegdate());
            dto.setUserid(comment.getMember().getUserid());
            return dto;
        }).collect(Collectors.toList());
    }

}
