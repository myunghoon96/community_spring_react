package com.example.demo.Api;

import com.example.demo.Dto.BoardDto;
import com.example.demo.Dto.RankDto;
import com.example.demo.Entity.Board;
import com.example.demo.Entity.Member;
import com.example.demo.Redis.RedisService;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.MemberRepository;
import com.example.demo.Service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/board")
public class BoardApiController {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final RedisService redisService;

    public BoardApiController(BoardRepository boardRepository, BoardService boardService, MemberRepository memberRepository, RedisService redisService){
        this.boardRepository = boardRepository;
        this.boardService = boardService;
        this.memberRepository = memberRepository;
        this.redisService = redisService;
    }

    @GetMapping("/rank")
    public ApiResponse<List<RankDto>> getRankList(){
        return ApiResponse.success(redisService.getRankList());
    }

    @GetMapping
    public ApiResponse<List<BoardDto>> boardList(){
        List<Board> boards = boardRepository.findAll();
        List<BoardDto> boardDtos = boards.stream().map(b-> new BoardDto(b.getMember().getEmail(), b)).collect(Collectors.toList());

        for (BoardDto b : boardDtos){
            int view = redisService.getViewByBoardTitle(b.getTitle());
            b.updateViewFromRedis(view);
        }

        return ApiResponse.success(boardDtos);
    }

    @PostMapping
    public ApiResponse<Long> addBoard(@RequestBody @Valid BoardDto boardDto) throws Exception{
        if (boardService.checkTitleDuplication(boardDto.getTitle())){
            throw new Exception("같은 제목을 가진 게시글이 존재합니다. 다른 제목을 사용해주세요.");
        }
        try {
            Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Board board = new Board(member, boardDto.getTitle(), boardDto.getContent());
            Long boardId = boardService.addBoard(board);
            redisService.addInToRankList(new RankDto(board.getTitle(), (double) board.getView()));
            return ApiResponse.success(boardId);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }


    @GetMapping("/{boardId}")
    public ApiResponse<BoardDto> boardDetail(@PathVariable Long boardId){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다"));

//        if (redisService.isFirstViewByEmail(member.getEmail(), boardId)){
////            boardService.updateView(boardId);
//            redisService.addViewByBoardId(boardId);
//            findBoard = boardRepository.findById(boardId).get();
//        }
        redisService.increaseViewByBoardTitle(findBoard.getTitle());
        int view = redisService.getViewByBoardTitle(findBoard.getTitle());

        BoardDto boardDto = new BoardDto(member.getEmail(), findBoard);
        boardDto.updateViewFromRedis(view);
        return ApiResponse.success(boardDto);
    }

    @GetMapping("/profile")
    public ApiResponse<List<BoardDto>> boardListByMember(){
        try {
            Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<Board> boards = boardRepository.findByMember(member);
            List<BoardDto> boardDtos = boards.stream().map(b-> new BoardDto(b.getMember().getEmail(), b)).collect(Collectors.toList());
            return ApiResponse.success(boardDtos);
        } catch (Exception e){
            throw new RuntimeException("현재 유저 정보를 확인할 수 없습니다.");
        }
    }

}
