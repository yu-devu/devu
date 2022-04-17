package com.devu.backend.service;

import com.devu.backend.config.auth.token.RefreshTokenRepository;
import com.devu.backend.config.auth.token.TokenService;
import com.devu.backend.entity.User;
import com.devu.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private TokenService tokenService;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private CookieService cookieService;

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository,passwordEncoder,tokenService,refreshTokenRepository,cookieService);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void createUser() {
        //given
        String email = "test@test.com";
        String authKey = "test";
        //when
        User user = userService.createUser(email, authKey);
        ReflectionTestUtils.setField(user,"id",1L);
        //then
        assertEquals(email, user.getEmail());
    }

    @Test
    void isEmailExists() {
    }

    @Test
    void getByEmail() {
    }

    @Test
    void updateUserAuthKey() {
    }

    @Test
    void getByAuthKey() {
    }

    @Test
    void updateUserConfirm() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getByCredentials() {
    }

    @Test
    void loginProcess() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void getUsers() {
    }
}