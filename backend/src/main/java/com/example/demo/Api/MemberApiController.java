package com.example.demo.Api;

import com.example.demo.Constant.Role;
import com.example.demo.Security.JwtProvider;
import com.example.demo.Dto.MemberDto;
import com.example.demo.Dto.MemberResponseDto;
import com.example.demo.Entity.Member;
import com.example.demo.Repository.MemberRepository;
import com.example.demo.Repository.RefreshTokenRepository;
import com.example.demo.Service.MemberService;
import com.example.demo.Service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/member")
public class MemberApiController {
    private final PasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;


    MemberApiController(PasswordEncoder bCryptPasswordEncoder, MemberRepository memberRepository, MemberService memberService, JwtProvider jwtProvider, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;

    }

    @GetMapping
    public ApiResponse<List<MemberResponseDto>> memberList(){
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> memberResponseDtos = members.stream()
                .map(m-> new MemberResponseDto(m.getId(), m.getEmail()))
                .collect(Collectors.toList());

        return ApiResponse.success(memberResponseDtos);
    }

    @Cacheable(value = "profile", key="#authentication.name", cacheManager = "cacheManager")
    @GetMapping("/profile")
    public ApiResponse<MemberResponseDto> profile(HttpServletRequest request, Authentication authentication){
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MemberResponseDto memberResponseDto = new MemberResponseDto(member.getEmail(), member.getRole());
        return ApiResponse.success(memberResponseDto);
    }


    @PostMapping("/signup")
    public ApiResponse<Null> joinMember(@RequestBody @Valid MemberDto memberDto) throws Exception {
        if (memberService.checkEmailDuplication(memberDto.getEmail())){
            throw new Exception("이미 사용중인 이메일입니다");
        }

        Member member = new Member(memberDto.getEmail(), memberDto.getPassword());
        member.encodePassword(bCryptPasswordEncoder);
        memberService.addMember(member);
        return ApiResponse.success(null);
    }

    @PostMapping("/login")
    public ApiResponse<Null> login(@RequestBody @Valid MemberDto memberDto, HttpServletResponse response) throws Exception {
        MemberResponseDto loginMemberDto = memberService.login(memberDto);
        String accessToken = jwtProvider.createAccessToken(String.valueOf(loginMemberDto.getEmail()), loginMemberDto.getRole());
        response.addCookie(createCookie("accessToken", accessToken, 3600));

        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(loginMemberDto.getEmail()), loginMemberDto.getRole());
        response.addCookie(createCookie("refreshToken", refreshToken, 3*3600));
        return ApiResponse.success(null);
    }


    @PostMapping("/oauth/kakao")
    public ApiResponse<String> loginByKakao(@RequestBody String token, HttpServletResponse response) throws Exception {
        String kakaoEmail = memberService.getEmailByKakaoToken(token);
        log.info("loginByKakao kakaoEmail {}", kakaoEmail);

        String accessToken = jwtProvider.createAccessToken(kakaoEmail, Role.USER);
        response.addCookie(createCookie("accessToken", accessToken, 3600));

        String refreshToken = jwtProvider.createRefreshToken(kakaoEmail, Role.USER);
        response.addCookie(createCookie("refreshToken", refreshToken, 3*3600));

        return ApiResponse.success(kakaoEmail);
    }

    @PostMapping("/refresh")
    public ApiResponse<Null> refresh(@RequestHeader(value="X-AUTH-TOKEN") String accessToken, @RequestHeader(value="REFRESH-TOKEN") String refreshToken, HttpServletResponse response) throws Exception {
        try {
            String email = getEmailFromExpiredAccessToken(accessToken);
            Member member = memberRepository.findByEmail(email).orElseThrow(IllegalAccessError::new);

            if (refreshTokenService.validation(email, refreshToken)){
                String newAccessToken = jwtProvider.createAccessToken(email, member.getRole());
                response.addCookie(createCookie("accessToken", newAccessToken,3600));
                return ApiResponse.success(null);
            }

            return ApiResponse.error(HttpStatus.BAD_REQUEST, "유효하지 않은 refresh 토큰입니다.");
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    public Cookie createCookie(String cookieName, String token, int maxAge){
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
        return cookie;
    }

    public String getEmailFromExpiredAccessToken(String accessToken){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = accessToken.split("\\.");
        String payloadFromExpiredAccessToken = new String(decoder.decode(parts[1]));
        payloadFromExpiredAccessToken = payloadFromExpiredAccessToken.substring(1, payloadFromExpiredAccessToken.length()-1);

        String email = payloadFromExpiredAccessToken.split(",")[0].split(":")[1];
        email = email.substring(1, email.length()-1);

        return email;
    }
}
