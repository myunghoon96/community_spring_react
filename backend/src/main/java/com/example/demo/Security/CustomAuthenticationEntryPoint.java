package com.example.demo.Security;

import com.example.demo.Api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String exception = (String) request.getAttribute("exception");
        AuthError authError;

        if(exception == null) {
            authError = AuthError.UNAUTHORIZEDException;
            setResponse(response, authError);
            log.warn(authError.getMessage());
        } else if (exception.equals("IllegalArgumentException")) {
            authError = AuthError.IllegalArgumentException;
            setResponse(response, authError);
            log.warn(authError.getMessage());
        } else if (exception.equals("MalformedJwtException")) {
            authError = AuthError.MalformedJwtException;
            setResponse(response, authError);
            log.warn(authError.getMessage());
        } else if (exception.equals("ReLogin")) {
            authError = AuthError.ReLogin;
            setResponse(response, authError);
            log.warn(authError.getMessage());
        } else{
            authError = AuthError.UNAUTHORIZEDException;
            setResponse(response, authError);
            log.warn(authError.getMessage());
        }
    }

    private void setResponse(HttpServletResponse response, AuthError authError) throws IOException {

        String jsonString = new ObjectMapper().writeValueAsString(ApiResponse.error(HttpStatus.valueOf(authError.getCode()), authError.getMessage()));

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        //relogin needed, both tokens are expired(invalid)
        if(authError.getStatus() == HttpStatus.NOT_ACCEPTABLE){
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        response.getWriter().print(jsonString);
    }

}
