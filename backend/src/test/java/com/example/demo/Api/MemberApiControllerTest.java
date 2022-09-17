package com.example.demo.Api;

import com.example.demo.Dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @DisplayName("전체 회원 조회")
    void getMemberListSuccess() throws Exception{
        String url = "/api/member";

        mockMvc.perform( MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("error").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws Exception {
        String signupUrl = "/api/member/signup";
        mockMvc.perform( MockMvcRequestBuilders
                        .post(signupUrl)
                        .content(asJsonString(new MemberDto("loginSuccess@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("data").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("error").doesNotExist())
                .andDo(print());

        String loginUrl = "/api/member/login";
        mockMvc.perform( MockMvcRequestBuilders
                        .post(loginUrl)
                        .content(asJsonString(new MemberDto("loginSuccess@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("data").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("error").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 성공")
    void joinMemberSuccess() throws Exception {
        String url = "/api/member/signup";

        mockMvc.perform( MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(new MemberDto("joinMemberSuccess@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("data").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("error").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 입력 정보 검증")
    void joinMemberFailByValidation() throws Exception {
        String url = "/api/member/signup";

        mockMvc.perform( MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(new MemberDto("", "")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("error").exists())
                .andDo(print());

    }

    @Test
    @DisplayName("회원가입 중복 이메일")
    void joinMemberFailByDuplicateEmail() throws Exception {
        String url = "/api/member/signup";

        mockMvc.perform( MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(new MemberDto("joinMemberFailByDuplicateEmail@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(IsNull.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("error").value(IsNull.nullValue()))
                .andDo(print());

        mockMvc.perform( MockMvcRequestBuilders
                        .post(url)
                        .content(asJsonString(new MemberDto("joinMemberFailByDuplicateEmail@test.com", "1234")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("data").value(IsNull.nullValue()))
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

}