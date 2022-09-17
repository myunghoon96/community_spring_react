package com.example.demo.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id @GeneratedValue
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String token;

    public RefreshToken(String email, String token){
        this.email = email;
        this.token = token;
    }
}
