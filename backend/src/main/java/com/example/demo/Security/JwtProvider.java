package com.example.demo.Security;

import com.example.demo.Constant.Role;
import com.example.demo.Repository.RefreshTokenRepository;
import com.example.demo.Service.RefreshTokenService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider{
    @Value("jwt.secret")
    private String secretKey;
    private Long accessTokenValidMillisecond = 1 * 60 * 60 * 1000L;
    private Long refreshTokenValidMillisecond = 3 * 60 * 60 * 1000L;
    private String issuer = "https://hoon.ml";

    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtProvider(CustomUserDetailsService userDetailsService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String email, Role role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Transactional
    public String createRefreshToken(String email, Role role) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        if (refreshTokenRepository.existsByEmail(email)){
            refreshTokenService.changeRefreshToken(email, refreshToken);
        }
        else{
            refreshTokenService.addRefreshToken(email, refreshToken);
        }
        return refreshToken;
    }

    public Authentication getAuthentication (String acessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccessTokenSubject(acessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //get email from access token
    public String getAccessTokenSubject(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveAcessToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }
    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader("REFRESH-TOKEN");
    }

    public boolean validationAccessToken(ServletRequest request, String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (MalformedJwtException e) {
            log.warn("access token MalformedJwtException {}", e.getMessage());
            request.setAttribute("exception", "MalformedJwtException");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", "ExpiredJwtException");
            String refreshToken = resolveRefreshToken((HttpServletRequest) request);
            if (validationRefreshToken(request, refreshToken)){
                log.warn("access token ExpiredJwtException {}", e.getMessage());
            }
            else {
                log.warn("access token ExpiredJwtException, refresh token invalid, need Relogin");
                request.setAttribute("exception", "ReLogin");
            }
        } catch (UnsupportedJwtException e) {
            log.warn("access token  UnsupportedJwtException {}", e.getMessage());
            request.setAttribute("exception", "UnsupportedJwtException");
        } catch (IllegalArgumentException e) {
            log.warn("access token IllegalArgumentException {}", e.getMessage());
            request.setAttribute("exception", "IllegalArgumentException");
        } catch (SignatureException e) {
            log.warn("access token SignatureException: {}", e.getMessage());
            request.setAttribute("exception", "SignatureException");
        }
        return false;
    }

    public boolean validationRefreshToken(ServletRequest request, String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (MalformedJwtException e) {
            log.warn("refresh token MalformedJwtException {}", e.getMessage());
            request.setAttribute("exception", "Relogin");
        } catch (ExpiredJwtException e) {
            log.warn("refresh token ExpiredJwtException {}", e.getMessage());
            request.setAttribute("exception", "Relogin");
        } catch (UnsupportedJwtException e) {
            log.warn("refresh token UnsupportedJwtException {}", e.getMessage());
            request.setAttribute("exception", "Relogin");
        } catch (IllegalArgumentException e) {
            log.warn("refresh token IllegalArgumentException {}", e.getMessage());
            request.setAttribute("exception", "Relogin");
        } catch (SignatureException e) {
            log.warn("refresh token SignatureException: {}", e.getMessage());
            request.setAttribute("exception", "Relogin");
        }
        return false;
    }
}
