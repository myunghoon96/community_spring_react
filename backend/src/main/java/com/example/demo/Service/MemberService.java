package com.example.demo.Service;

import com.example.demo.Dto.MemberDto;
import com.example.demo.Dto.MemberResponseDto;
import com.example.demo.Entity.Member;
import com.example.demo.Repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

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

}
