package com.example.demo.Service;

import com.example.demo.Dto.MemberDto;
import com.example.demo.Dto.MemberResponseDto;
import com.example.demo.Entity.Member;
import com.example.demo.Repository.MemberRepository;
import com.fasterxml.jackson.core.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service
public class MemberService{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void insertSampleDatas(){
        Member member1 = new Member("hoon" + "@gmail.com", passwordEncoder.encode("password"));
        memberRepository.save(member1);

        Member member2 = new Member("tom" + "@gmail.com", passwordEncoder.encode("password"));
        memberRepository.save(member2);

        Member member3 = new Member("david" + "@gmail.com", passwordEncoder.encode("password"));
        memberRepository.save(member3);
    }

    public Long addMember(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    public MemberResponseDto login(MemberDto memberDto) throws Exception {
        Member member = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다."));
        if (passwordEncoder.matches(memberDto.getPassword(), member.getPassword())){
            return new MemberResponseDto(member.getId(),member.getEmail(),member.getRole());
        }
        else {
            throw new Exception("아이디 비밀번호가 틀립니다");
        }
    }

    public MemberResponseDto profile(MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다"));
        return new MemberResponseDto(member.getId(),member.getEmail(),member.getRole());
    }

    public boolean checkEmailDuplication(String email) {
        return memberRepository.existsByEmail(email);
    }

    public String getEmailByKakaoToken(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        JSONObject responseBody = stringToJsonObj(response.getBody());
        String kakaoId = responseBody.get("id").toString();
        JSONObject kakaoAccount = stringToJsonObj(responseBody.get("kakao_account").toString());
        String kakaoEmail = kakaoAccount.get("email").toString();

        if (memberRepository.existsByEmail(kakaoEmail) == false){
            Member member = new Member(kakaoEmail, kakaoId);
            member.encodePassword(passwordEncoder);
            memberRepository.save(member);
        }

        log.info(kakaoEmail);
        return kakaoEmail;
    }

    public JSONObject stringToJsonObj(String inputString){
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse( inputString );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObj = (JSONObject) obj;
        return jsonObj;
    }
}
