package com.example.demo.Security;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum AuthError {

    UNAUTHORIZEDException (401, "로그인 후 이용가능합니다.", HttpStatus.UNAUTHORIZED),
    ExpiredJwtException(401, "기존 토큰이 만료되었습니다. 다시 로그인 해주세요.", HttpStatus.UNAUTHORIZED),
    ReLogin(406, "모든 토큰이 만료되었습니다. 다시 로그인해주세요.", HttpStatus.NOT_ACCEPTABLE),
    IllegalArgumentException(401, "토큰이 헤더에 포함되어 있지 않습니다.", HttpStatus.UNAUTHORIZED),
    MalformedJwtException(401, "올바르지 않은 토큰입니다", HttpStatus.UNAUTHORIZED);

    @Getter
    private int code;

    @Getter
    private String message;

    @Getter
    private HttpStatus status;

    AuthError(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}