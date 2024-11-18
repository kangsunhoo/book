package com.example.portfolio.controller;

import com.example.portfolio.dto.BoardDTO;
import com.example.portfolio.dto.BookDTO;
import com.example.portfolio.dto.CommentDTO;
import com.example.portfolio.dto.ReviewDTO;
import com.example.portfolio.entity.BoardEntity;
import com.example.portfolio.entity.BookEntity;
import com.example.portfolio.repository.BoardRepository;
import com.example.portfolio.service.BoardServiceImpl;
import com.example.portfolio.service.CommentService;

import com.example.portfolio.service.CommentServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@AllArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardService;
    private final BoardRepository boardRepository;
    private final CommentServiceImpl commentService;


    // 게시글 목록 화면 (board_list.html)
    @GetMapping("/board/list")
    public String boardList(Model model, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        model.addAttribute("userid", userid);
        String userRole = (String) session.getAttribute("role"); // 역할 정보 가져오기

        List<BoardDTO> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        model.addAttribute("role", userRole); // 역할 정보 추가
        return "board/board_list";
    }

    // 게시글 작성 폼 (board.html)
    @GetMapping("/board/new")
    public String newBoardForm(Model model, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        String nickname = (String) session.getAttribute("nickname");

        if (userid == null) {
            return "redirect:/login";  // 로그인되지 않은 사용자라면 로그인 페이지로 리다이렉트
        }

        model.addAttribute("userid", userid);
        model.addAttribute("nickname", nickname);
        model.addAttribute("boardDTO", new BoardDTO());

        return "board/board";
    }

    @PostMapping("/board/save")
    public String saveBoard(
            @RequestParam("title") String title,
            @RequestParam("conte") String conte,
            HttpSession session) throws IOException {

        String userid = (String) session.getAttribute("userid");

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setUserid(userid);
        boardDTO.setTitle(title);
        boardDTO.setConte(conte);
        boardDTO.setHit(0);
        boardDTO.setLikes(0);
        boardDTO.setRegdate(LocalDate.now());

        boardService.saveBoard(boardDTO);

        return "redirect:/board/list";
    }

    @GetMapping("/board/{idx}")
    public String viewBoard(@PathVariable Long idx, Model model, HttpSession session) {
        String userid = (String) session.getAttribute("userid");
        String role = (String) session.getAttribute("role");  // 세션에서 role 가져오기

        model.addAttribute("role", role);  // 역할 정보를 모델에 추가
        // 사용자 ID를 세션에서 가져오기
        model.addAttribute("userid", userid);

        // 게시판 데이터 가져오기
        BoardDTO boardDTO = boardService.getBoardById(idx);
        BoardDTO previousBoard = boardService.getPreviousBoard(idx);
        BoardDTO nextBoard = boardService.getNextBoard(idx);

        model.addAttribute("board", boardDTO);
        model.addAttribute("previousBoard", previousBoard);
        model.addAttribute("nextBoard", nextBoard);
        // 해당 게시물에 대한 댓글 가져오기
        List<CommentDTO> comments = commentService.getCommentsByBoardIdx(idx); // 메소드 이름 수정
        if (comments == null) {
            comments = new ArrayList<>(); // null인 경우 빈 리스트로 초기화
        }
        model.addAttribute("comments", comments); // 댓글 리스트를 모델에 추가

        return "board/board_detail"; // 게시판 상세 페이지로 이동
    }


    @PostMapping("/board/deleteSelected")
    public String deleteSelectedBoards(@RequestParam("selectedIds") List<Long> selectedIds) {
        // 선택된 게시글 삭제
        boardService.deleteSelectedBoards(selectedIds);
        return "redirect:/board/list";  // 삭제 후 목록 페이지로 리다이렉트
    }

    @PostMapping("/board/like/{idx}")
    public String likeBoard(@PathVariable Long idx, HttpSession session) {
        String userid = (String) session.getAttribute("userid");

        if (userid == null) {
            return "redirect:/login";  // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        }

        try {
            boardService.toggleLikes(idx, userid);  // 좋아요 토글 메서드 호출
        } catch (IllegalStateException e) {
            return "redirect:/board/" + idx + "?error=alreadyLiked"; // 중복 좋아요 시 에러 메시지 전달
        }

        return "redirect:/board/" + idx;
    }


    @GetMapping("/board_modify")
    public String board_modify(@RequestParam(value = "idx") Long idx, Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");

        // 관리자만 접근 가능
        if (!"관리자".equals(role)) {
            return "redirect:/access_denied"; // 접근 거부 페이지 또는 다른 페이지로 리다이렉트
        }

        String userid = (String) session.getAttribute("userid");
        model.addAttribute("userid", userid);

        BoardEntity board = boardRepository.findById(idx)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다. idx: " + idx));
        model.addAttribute("board", board);

        return "board/board_modify"; // 수정 페이지로 이동
    }


    @PostMapping("/board_modify_proc")
    public String board_modify_proc(@ModelAttribute BoardDTO boardDTO) {
        if (boardDTO.getIdx() == null) {
            throw new IllegalArgumentException("Idx 가 전달되지 않았습니다.");
        }
        boardService.modifyboard(boardDTO);
        return "redirect:/board/" + boardDTO.getIdx(); // 수정 후 상세 페이지로 리다이렉트
    }



    @GetMapping("/board/top5")
    @ResponseBody
    public List<BoardDTO> getTop5LikedBoards() {
        List<BoardEntity> boardEntities = boardRepository.findTop5ByOrderByLikesDesc();
        return boardEntities.stream()
                .map(board -> new BoardDTO(board.getIdx(), board.getMember().getUserid(), board.getTitle(), board.getLikes()))
                .collect(Collectors.toList());
    }


}

