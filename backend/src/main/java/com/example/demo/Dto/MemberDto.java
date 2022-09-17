package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    @NotBlank(message = "이메일은 필수 값 입니다")
    @Email(message = "올바른 이메일을 입력하세요")
    private String email;
    @NotBlank(message = "비밀번호는 필수 값 입니다")
    private String password;

    public MemberDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
