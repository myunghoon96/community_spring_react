package com.example.demo.Service;

import com.example.demo.Entity.RefreshToken;
import com.example.demo.Repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Transactional
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Long addRefreshToken(String email, String token){
        RefreshToken refreshToken = new RefreshToken(email, token);
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getId();
    }

    public boolean validation(String email, String token){
        RefreshToken findRefreshToken = refreshTokenRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
        if (findRefreshToken.getToken().equals(token)){
            return true;
        }
        return false;
    }

    public void changeRefreshToken(String email, String refreshToken) {
        refreshTokenRepository.updateToken(refreshToken, email);
    }
}
