package com.devu.backend.config.auth.token;

import com.devu.backend.config.auth.UserDetailsImpl;
import com.devu.backend.config.auth.UserDetailsServiceImpl;
import com.devu.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private final String secretKey = Base64.getEncoder().encodeToString("test".getBytes());
    private final long accessTokenValidTime = 100000;
    private final long refreshTokenValidTime = 500000;

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("액세스 토큰 생성")
    void createAccessToken() {
        String test = "test@gmail.com";

        String jwt = tokenService.createTestToken(secretKey, test, accessTokenValidTime);
        String subject = decodeJwt(jwt).getSubject();
        Date issuedAt = decodeJwt(jwt).getIssuedAt();
        Date expiration = decodeJwt(jwt).getExpiration();

        assertEquals(subject, test);
        assertEquals(getStartTime(expiration, accessTokenValidTime), issuedAt.getTime());
    }

    private long getStartTime(Date expiration, long accessTokenValidTime) {
        return expiration.getTime() - accessTokenValidTime;
    }

    private Claims decodeJwt(String jwt) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(jwt).getBody();
    }

    @Test
    @DisplayName("리프레쉬 토큰 생성")
    void createRefreshToken() {
        String test = "test@gmail.com";

        String jwt = tokenService.createTestToken(secretKey, test, refreshTokenValidTime);
        String subject = decodeJwt(jwt).getSubject();
        Date issuedAt = decodeJwt(jwt).getIssuedAt();
        Date expiration = decodeJwt(jwt).getExpiration();

        assertEquals(subject, test);
        assertEquals(getStartTime(expiration, refreshTokenValidTime), issuedAt.getTime());
    }

    @Test
    @DisplayName("인증 얻기")
    void getAuthentication() {
        String test = "test@gmail.com";
        User user = User.builder()
                .email(test)
                .build();
        UserDetails userDetails = new UserDetailsImpl(user);
        String jwt = tokenService.createTestToken(secretKey, test, accessTokenValidTime);
        given(userDetailsService.loadUserByUsername(test)).willReturn(userDetails);

        Authentication authentication = tokenService.getTestAuthentication(jwt, secretKey);

        assertEquals(((UserDetails)authentication.getPrincipal()).getUsername(), test);
    }

    @Test
    @DisplayName("토큰으로 부터 유저 이메일 얻기")
    void getUserEmail() {
        String test = "test@gmail.com";
        String jwt = tokenService.createTestToken(secretKey, test, accessTokenValidTime);

        String email = tokenService.getTestUserEmail(jwt, secretKey);

        assertEquals(email, test);
    }

    @Test
    @DisplayName("헤더에서 액세스 토큰 얻기")
    void resolveAccessToken() {
        String test = "test@gmail.com";
        String jwt = tokenService.createTestToken(secretKey, test, accessTokenValidTime);
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getHeader("X-AUTH-ACCESS-TOKEN")).willReturn(jwt);

        String resolveToken = tokenService.resolveToken(request, "ACCESS");

        assertEquals(resolveToken, jwt);
    }

    @Test
    @DisplayName("토큰 시간이 유효한지 확인 - 유효")
    void isTokenExpired_valid() {
        String test = "test@gmail.com";

        String jwt = tokenService.createTestToken(secretKey, test, accessTokenValidTime);
        Boolean tokenExpired = tokenService.isTestTokenExpired(jwt, secretKey);

        assertEquals(tokenExpired, true);
    }

    @Test
    @DisplayName("토큰이 시간이 유효한지 확인 - 무효")
    void isTokenExpired_invalid() {
        String test = "test@gmail.com";
        String jwt = tokenService.createTestToken(secretKey, test, 0);

        assertThrows(ExpiredJwtException.class, () -> {
            tokenService.isTestTokenExpired(jwt, secretKey);
        });
    }

    @Test
    @DisplayName("토큰 자체가 유효한지 - 유효")
    void validateToken_valid() {
        String test = "test@gmail.com";
        User user = User.builder()
                .email(test)
                .build();
        String jwt = tokenService.createTestToken(secretKey, test, accessTokenValidTime);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        assertTrue(tokenService.validateTestToken(jwt, userDetails, secretKey));
    }

    @Test
    @DisplayName("토큰 자체가 유효한지 - 무효")
    void validateToken_invalid() {
        String test = "test@gmail.com";
        String test1 = "test1@gmail.com";
        User user = User.builder()
                .email(test1)
                .build();
        String jwt = tokenService.createTestToken(secretKey, test, accessTokenValidTime);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        assertFalse(tokenService.validateToken(jwt, userDetails));
    }
}