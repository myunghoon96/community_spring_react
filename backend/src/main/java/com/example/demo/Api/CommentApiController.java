package com.example.demo.Api;

import com.example.demo.Dto.CommentDto;
import com.example.demo.Entity.Board;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Member;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Service.BoardService;
import com.example.demo.Service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/comment")
public class CommentApiController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final BoardRepository boardRepository;
    private final BoardService boardService;

    public CommentApiController(CommentRepository commentRepository, CommentService commentService, BoardRepository boardRepository, BoardService boardService ){
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.boardRepository = boardRepository;
        this.boardService = boardService;
    }

    @GetMapping("/board/{BoardId}")
    public ApiResponse<List<CommentDto>> commentListByBoardId(@PathVariable Long BoardId){
        Board board = boardRepository.findById(BoardId).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다"));

        List<Comment> comments = commentRepository.findByBoard(board);
        List<CommentDto> commentDtos = comments.stream().map(c-> new CommentDto(c)).collect(Collectors.toList());
        return ApiResponse.success(commentDtos);
    }

    @PostMapping("/board/{boardId}")
    public ApiResponse<Null> addComment(@PathVariable Long boardId, @RequestBody @Valid CommentDto commentDto){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다"));
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .member(member)
                .board(board)
                .build();

        commentService.addComment(comment);
        return ApiResponse.success(null);
    }

    @GetMapping("/profile")
    public ApiResponse<List<CommentDto>> commentListByMember(){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Comment> comments = commentRepository.findByMember(member);
        List<CommentDto> commentDtos = comments.stream().map(c-> new CommentDto(c)).collect(Collectors.toList());
        return ApiResponse.success(commentDtos);
    }



}
