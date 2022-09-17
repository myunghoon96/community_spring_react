package com.example.demo.Dto;

import com.example.demo.Constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberResponseDto implements Serializable {
    private Long id;
    @NotBlank(message = "이메일은 필수 값 입니다")
    @Email(message = "올바른 이메일을 입력하세요")
    private String email;
    private Role role;

    public MemberResponseDto(Long id, String email){
        this.id = id;
        this.email = email;
    }

    public MemberResponseDto(String email, Role role) {
        this.email = email;
        this.role = role;
    }
}
