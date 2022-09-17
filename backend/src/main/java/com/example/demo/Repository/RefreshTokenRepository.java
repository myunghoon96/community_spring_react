package com.example.demo.Repository;

import com.example.demo.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByToken(String token);
    boolean existsByEmail(String email);
    Optional<RefreshToken> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.token = :token WHERE rt.email = :email")
    void updateToken(@Param("token") String token, @Param("email") String email);

}
