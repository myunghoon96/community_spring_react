package com.example.demo.Api;

import com.example.demo.Dto.BoardDto;
import com.example.demo.Dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 전체 조회")
    void getBoardListSuccess() throws Exception{
        String accessToken = getAccessToken();
        String url = "/api/board/";

        mockMvc.perform( MockMvcRequestBuilders
                        .get(url)
                        .header("X-AUTH-TOKEN", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("error").doesNotExist())
                .andDo(print());
    }


    @Test
    @DisplayName("게시글 추가 성공")
    void addBoardSuccess() throws Exception{
        String accessToken = getAccessToken();
        String url = "/api/board/";

        mockMvc.perform( MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(new BoardDto("title1", "content1")))
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
    @DisplayName("게시글 추가 입력 정보 검증")
    void addBoardFailByValidation() throws Exception{
        String accessToken = getAccessToken();
        String url = "/api/board/";

        mockMvc.perform( MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(new BoardDto("", "")))
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
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String getAccessToken() throws Exception{

        String signupUrl = "/api/member/signup";
        mockMvc.perform( MockMvcRequestBuilders
                        .post(signupUrl)
                        .content(asJsonString(new MemberDto("test1@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String loginUrl = "/api/member/login";
        MvcResult loginResponse = mockMvc.perform(MockMvcRequestBuilders
                        .post(loginUrl)
                        .content(asJsonString(new MemberDto("test1@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        return loginResponse.getResponse().getCookie("accessToken").getValue();
    }
}

