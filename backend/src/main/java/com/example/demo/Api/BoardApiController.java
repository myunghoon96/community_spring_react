package com.example.demo.Api;

import com.example.demo.Dto.BoardDto;
import com.example.demo.Dto.PageDto;
import com.example.demo.Dto.PageInfo;
import com.example.demo.Dto.RankDto;
import com.example.demo.Entity.Board;
import com.example.demo.Entity.Member;
import com.example.demo.Redis.RedisService;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.MemberRepository;
import com.example.demo.Service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
    public ApiResponse<PageDto<List<BoardDto>>> boardList(@Positive @RequestParam int page, @Positive @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Board> pageBoard= boardRepository.findAll(pageRequest);

        List<Board> boards = pageBoard.getContent();
        List<BoardDto> boardDtos = boards.stream().map(b-> new BoardDto(b.getMember().getEmail(), b)).collect(Collectors.toList());

        for (BoardDto b : boardDtos){
            int view = redisService.getViewByBoardTitle(b.getTitle());
            if (view == -1){
                view = b.getView();
                redisService.addInToRankList(new RankDto(b.getTitle(), (double) b.getView()));
            }
            b.updateViewFromRedis(view);
        }

        PageDto<List<BoardDto>> pageDto = new PageDto<>(boardDtos, new PageInfo(page, size, pageBoard.getNumberOfElements(), pageBoard.getTotalPages()));
        return ApiResponse.success(pageDto);
    }

//    @GetMapping
//    public ApiResponse<List<BoardDto>> boardList(){
//        List<Board> boards = boardRepository.findAll();
//        List<BoardDto> boardDtos = boards.stream().map(b-> new BoardDto(b.getMember().getEmail(), b)).collect(Collectors.toList());
//
//        for (BoardDto b : boardDtos){
//            int view = redisService.getViewByBoardTitle(b.getTitle());
//            if (view == -1){
//                view = b.getView();
//                redisService.addInToRankList(new RankDto(b.getTitle(), (double) b.getView()));
//            }
//            b.updateViewFromRedis(view);
//        }
//
//        return ApiResponse.success(boardDtos);
//    }

    @PostMapping
    public ApiResponse<Long> addBoard(@RequestBody @Valid BoardDto boardDto) throws Exception{
        if (boardService.checkTitleDuplication(boardDto.getTitle())){
            throw new Exception("?????? ????????? ?????? ???????????? ???????????????. ?????? ????????? ??????????????????.");
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
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("???????????? ???????????? ?????? ??? ????????????"));

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
            List<BoardDto> boardDtos = boards.stream().map(b-> new BoardDto(b.getMember().getEmail(), b, redisService.getViewByBoardTitle(b.getTitle()))).collect(Collectors.toList());
            return ApiResponse.success(boardDtos);
        } catch (Exception e){
            throw new RuntimeException("?????? ?????? ????????? ????????? ??? ????????????.");
        }
    }

}
