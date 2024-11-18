package com.example.portfolio.service;

import com.example.portfolio.dto.BoardDTO;
import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.entity.BoardEntity;
import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.entity.MemberEntity;
import com.example.portfolio.repository.BoardRepository;
import com.example.portfolio.repository.MemberRepository;
import com.example.portfolio.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void saveBoard(BoardDTO boardDTO) {
        MemberEntity member = memberRepository.findByUserid(boardDTO.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자(ID)가 존재하지 않습니다: " + boardDTO.getUserid()));

        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setMember(member);
        boardEntity.setConte(boardDTO.getConte());
        boardEntity.setRegdate(LocalDateTime.now());
        boardEntity.setHit(boardDTO.getHit());
        boardEntity.setLikes(boardDTO.getLikes());

        boardRepository.save(boardEntity);
    }

    @Override
    public List<BoardDTO> getAllBoards() {
        return boardRepository.findAllByOrderByRegdateDesc().stream()
                .map(board -> {
                    BoardDTO boardDTO = new BoardDTO();
                    boardDTO.setIdx(board.getIdx());
                    boardDTO.setTitle(board.getTitle());
                    boardDTO.setConte(board.getConte());
                    boardDTO.setRegdate(board.getRegdate().toLocalDate());
                    boardDTO.setHit(board.getHit());
                    boardDTO.setLikes(board.getLikes());
                    boardDTO.setUserid(board.getMember().getUserid());
                    return boardDTO;
                }).collect(Collectors.toList());
    }


    @Override
    public BoardDTO getBoardById(Long idx) {
        return boardRepository.findById(idx).map(board -> {
            board.setHit(board.getHit() + 1);
            boardRepository.save(board);

            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setIdx(board.getIdx());
            boardDTO.setTitle(board.getTitle());
            boardDTO.setConte(board.getConte());
            boardDTO.setRegdate(board.getRegdate().toLocalDate());
            boardDTO.setHit(board.getHit());
            boardDTO.setLikes(board.getLikes());
            boardDTO.setUserid(board.getMember().getUserid());

            return boardDTO;
        }).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
    }

    @Override
    public void deleteSelectedBoards(List<Long> selectedIds) {
        boardRepository.deleteAllById(selectedIds);
    }
    @Override
    public void toggleLikes(Long idx, String userid) {
        BoardEntity boardEntity = boardRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        if (boardEntity.hasUserLiked(userid)) {
            // 좋아요 취소
            boardEntity.removeUserIdFromLikes(userid);  // 사용자 ID 제거 메서드 호출
            boardEntity.setLikes(boardEntity.getLikes() - 1);  // 좋아요 수 감소
        } else {
            // 좋아요 추가
            boardEntity.addUserIdToLikes(userid);
        }

        boardRepository.save(boardEntity);
    }


    @Override
    public BoardEntity board_modify(Long idx) {
        // 회원 수정 정보 조회
        if (idx != null && idx != 0) {
            return boardRepository.getReferenceById(idx);
        }
        return null;
    }


    public void modifyboard(BoardDTO boardDTO) {

        MemberEntity member = memberRepository.findByUserid(boardDTO.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자(ID)가 존재하지 않습니다: " + boardDTO.getUserid()));
        // 회원 정보 수정
        BoardEntity boardEntity = boardRepository.getReferenceById(boardDTO.getIdx());

        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setMember(member);
        boardEntity.setConte(boardDTO.getConte());
        boardEntity.setRegdate(LocalDateTime.now());
        boardEntity.setHit(boardDTO.getHit());
        boardEntity.setLikes(boardDTO.getLikes());

        boardRepository.save(boardEntity);
    }

    @Override
    public BoardDTO getPreviousBoard(Long idx) {
        return boardRepository.findTopByIdxLessThanOrderByIdxDesc(idx)
                .map(board -> {
                    BoardDTO boardDTO = new BoardDTO();
                    boardDTO.setIdx(board.getIdx());
                    boardDTO.setTitle(board.getTitle());
                    boardDTO.setRegdate(LocalDate.from(board.getRegdate()));
                    return boardDTO;
                }).orElse(null);
    }

    @Override
    public BoardDTO getNextBoard(Long idx) {
        return boardRepository.findTopByIdxGreaterThanOrderByIdxAsc(idx)
                .map(board -> {
                    BoardDTO boardDTO = new BoardDTO();
                    boardDTO.setIdx(board.getIdx());
                    boardDTO.setTitle(board.getTitle());
                    boardDTO.setRegdate(LocalDate.from(board.getRegdate()));
                    return boardDTO;
                }).orElse(null);
    }



}
