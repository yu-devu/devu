package com.devu.backend.config.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private String secretKey = "Devu";
    private long accessTokenValidTime = 1000L * 60 * 30; // 30분
    private long refreshTokenValidTime = 1000L * 60 * 30 * (2 * 24 * 14); // 14일

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("액세스 토큰 생성")
    void createAccessToken() {
        String test = "test@gmail.com";

        String jwt = tokenService.createAccessToken(test);
        String subject = decodeJwt(jwt).getSubject();
        Date issuedAt = decodeJwt(jwt).getIssuedAt();
        Date expiration = decodeJwt(jwt).getExpiration();

        assertEquals(subject, test);
        assertEquals(expiration.getTime() - accessTokenValidTime, issuedAt.getTime());
    }

    private Claims decodeJwt(String jwt) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(jwt).getBody();
    }

    @Test
    @DisplayName("리프레쉬 토큰 생성")
    void createRefreshToken() {
        String test = "test@gmail.com";

        String jwt = tokenService.createRefreshToken(test);
        String subject = decodeJwt(jwt).getSubject();
        Date issuedAt = decodeJwt(jwt).getIssuedAt();
        Date expiration = decodeJwt(jwt).getExpiration();

        assertEquals(subject, test);
        assertEquals(expiration.getTime() - refreshTokenValidTime, issuedAt.getTime());
    }
}