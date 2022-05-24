package com.devu.backend.config.auth.token;

import com.devu.backend.config.auth.UserDetailsImpl;
import com.devu.backend.config.auth.UserDetailsServiceImpl;
import com.devu.backend.repository.RefreshTokenRepository;
import com.devu.backend.service.CookieService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;
    private final UserDetailsServiceImpl userDetailsServiceimpl;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = tokenService.resolveToken((HttpServletRequest) request, "ACCESS");
        String refreshToken = null;

        try {
            if (accessToken != null && !accessToken.equals("undefined") && !accessToken.equals("null")) {
                UserDetailsImpl userDetailsimpl = (UserDetailsImpl) userDetailsServiceimpl.loadUserByUsername(tokenService.getUserEmail(accessToken));
                if (tokenService.validateTokenExceptExpiration(accessToken, userDetailsimpl)) {
                    log.info("유효");
                    Authentication auth = tokenService.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    log.info("유효x");
                    refreshToken = existRefreshToken((HttpServletRequest) request, refreshToken);
                }
            }else{
                log.info("accessToken x");
                refreshToken = existRefreshToken((HttpServletRequest) request, refreshToken);
            }
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
        }

        try {
            if (refreshToken != null) {
                log.info("refresh");
                RefreshToken dbRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
                if (dbRefreshToken != null && tokenService.isTokenExpired(dbRefreshToken.getRefreshToken())) {
                    Authentication auth = tokenService.getAuthentication(dbRefreshToken.getRefreshToken());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
                    ((HttpServletResponse) response).setHeader("X-AUTH-ACCESS-TOKEN", tokenService.createAccessToken(principal.getUsername()));
                }
            }
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
        }
        chain.doFilter(request, response);
    }

    private String existRefreshToken(HttpServletRequest request, String refreshToken) {
        Cookie cookie = cookieService.getCookie(request, "X-AUTH-REFRESH-TOKEN");
        if (cookie != null) {
            refreshToken = cookie.getValue();
        }
        return refreshToken;
    }
}
