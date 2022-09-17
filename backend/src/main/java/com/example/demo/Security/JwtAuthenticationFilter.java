package com.example.demo.Security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse httpServeletResponse = (HttpServletResponse) response;
        httpServeletResponse.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
        httpServeletResponse.setHeader("Access-Control-Allow-Credentials",  "true");

        String token = jwtProvider.resolveAcessToken((HttpServletRequest) request);
        if (token != null && jwtProvider.validationAccessToken(request, token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, httpServeletResponse);
    }

}
