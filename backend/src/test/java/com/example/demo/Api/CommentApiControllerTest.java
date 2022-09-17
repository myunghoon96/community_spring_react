package com.example.demo.Api;

import com.example.demo.Dto.BoardDto;
import com.example.demo.Dto.CommentDto;
import com.example.demo.Dto.MemberDto;
import com.example.demo.Entity.Board;
import com.example.demo.Entity.Member;
import com.example.demo.Repository.BoardRepository;
import com.example.demo.Repository.MemberRepository;
import com.example.demo.Service.BoardService;
import com.example.demo.Service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;



    @Test
    @Transactional
    @DisplayName("댓글 추가 성공")
    void addCommentSuccess() throws Exception {
        String accessToken = getAccessToken();
        Long memberId = memberService.addMember(new Member("addCommentSuccess@test.com", "1234"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다"));
        Long boardId = boardService.addBoard(new Board(member,"addCommentSuccess", "addCommentSuccess"));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다"));
        CommentDto commentDto = new CommentDto("addCommentSuccess", member, board);
        String url = "/api/comment/board/" + boardId;

        mockMvc.perform( MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(commentDto))
                        .header("X-AUTH-TOKEN", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("data").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("error").doesNotExist())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("댓글 추가 입력 정보 검증")
    void addCommentFail() throws Exception {
        String accessToken = getAccessToken();
        Long memberId = memberService.addMember(new Member("addCommentFail@test.com", "1234"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다"));
        Long boardId = boardService.addBoard(new Board(member,"addCommentFail", "addCommentFail"));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다"));
        CommentDto commentDto = new CommentDto("", member, board);
        String url = "/api/comment/board/" + boardId;

        mockMvc.perform( MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(commentDto))
                        .header("X-AUTH-TOKEN", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("data").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("error").exists())
                .andDo(print());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAccessToken() throws Exception{

        String signupUrl = "/api/member/signup";
        mockMvc.perform( MockMvcRequestBuilders
                        .post(signupUrl)
                        .content(asJsonString(new MemberDto("getAccessToken@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String loginUrl = "/api/member/login";
        MvcResult loginResponse = mockMvc.perform(MockMvcRequestBuilders
                        .post(loginUrl)
                        .content(asJsonString(new MemberDto("getAccessToken@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        return loginResponse.getResponse().getCookie("accessToken").getValue();
    }

}