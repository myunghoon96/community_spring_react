package com.example.demo.Service;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Member;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 추가 성공")
    void addComment() {
        Member member = new Member("test@test.com", "password");
        Long memberId = memberService.addMember(member);
        Board board = new Board(member,"title1", "content1");
        Long boardId = boardService.addBoard(board);

        Comment comment = Comment.builder()
                .content("comment content")
                .member(member)
                .board(board)
                .build();
        Long commentId = commentService.addComment(comment);
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당하는 댓글을 찾을 수 없습니다"));

        Assertions.assertEquals(memberId, findComment.getMember().getId());
        Assertions.assertEquals(boardId, findComment.getBoard().getId());
        Assertions.assertEquals("comment content", findComment.getContent());

    }
}