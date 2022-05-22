package com.devu.backend.repository;

import com.devu.backend.config.auth.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByRefreshToken(String refreshToken);
}
