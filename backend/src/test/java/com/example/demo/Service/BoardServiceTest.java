package com.example.demo.Service;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Member;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
//@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("게시글 추가 성공")
    void addBoard() {
        String email = "test@test.com";
        String password = "1234";
        Member member = new Member(email, password);
        memberService.addMember(member);

        Board board = new Board(member,"title1", "content1");
        Long boardId = boardService.addBoard(board);
        Optional<Board> findBoard = boardRepository.findById(boardId);

        Assertions.assertEquals(email, findBoard.get().getMember().getEmail());
        Assertions.assertEquals("title1", findBoard.get().getTitle());
        Assertions.assertEquals("content1", findBoard.get().getContent());
    }

    @Test
    @DisplayName("게시글 조회수")
    void updateBoardView() {
        String email = "test2@test.com";
        String password = "1234";
        Member member = new Member(email, password);
        memberService.addMember(member);

        Board board = new Board(member,"title2", "content2");
        Long boardId = boardService.addBoard(board);

        boardService.updateView(boardId, 1);
        Optional<Board> findBoard = boardRepository.findById(boardId);

        Assertions.assertEquals(1, findBoard.get().getView());
    }

    @Test
    @Transactional(Transactional.TxType.NEVER)
    @DisplayName("게시글 조회수 동시성")
    void updateBoardViewConcurrency() throws InterruptedException {

        String email = "test3@test.com";
        String password = "1234";
        Member member = new Member(email, password);
        memberService.addMember(member);

        Board board = new Board(member,"title3", "content3");
        Long boardId = boardService.addBoard(board);

        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        //when
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                boardService.updateView(boardId, 1);
                countDownLatch.countDown();
            }
            );
        }
        countDownLatch.await();

        Optional<Board> findBoard = boardRepository.findById(boardId);
        Assertions.assertEquals(numberOfThreads, findBoard.get().getView());

    }

    @AfterEach
    void deleteAllBoards(){
        boardRepository.deleteAll();
    }

}