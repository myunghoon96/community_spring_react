package com.example.demo.Service;

import com.example.demo.Entity.Member;
import com.example.demo.Repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    void addMember() {
        Member member = new Member("test@test.com", "1234");
        member.encodePassword(passwordEncoder);
        Long memberId = memberService.addMember(member);

        Optional<Member> findMember = memberRepository.findById(memberId);

        Assertions.assertEquals(memberId, findMember.get().getId());
        Assertions.assertEquals("test@test.com", findMember.get().getEmail());
        Assertions.assertTrue(passwordEncoder.matches("1234",findMember.get().getPassword()));
    }
}