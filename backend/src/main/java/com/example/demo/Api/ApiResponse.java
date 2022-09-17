package com.example.demo.Api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Null;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ApiError error;

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<T>(true, data, null);
    }

    public static ApiResponse<Null> error(HttpStatus status, String msg){
        return new ApiResponse<>(false, null, new ApiError(status, msg));
    }
}
