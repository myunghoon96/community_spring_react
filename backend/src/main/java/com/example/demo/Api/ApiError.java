package com.example.demo.Api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@ToString
@Getter
public class ApiError {

    private HttpStatus status;
    private String msg;
}
