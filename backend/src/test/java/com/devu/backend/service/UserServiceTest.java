package com.devu.backend.service;

import com.devu.backend.config.auth.token.RefreshTokenRepository;
import com.devu.backend.config.auth.token.TokenService;
import com.devu.backend.entity.User;
import com.devu.backend.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private CookieService cookieService;

    @Mock
    private RefreshTokenRepository tokenRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    void createUser() throws Exception {
        String email = "test@test.com";
        String authKey = "test";

        //given
        User user = User.builder()
                .email(email)
                .emailAuthKey(authKey)
                .build();

        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(user,"id",fakeUserId);

        //Mocking
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        User save = userService.createUser(email);


        //then
        Assertions.assertThat(save.getId()).isEqualTo(1L);
        //assertEquals(user.getUsername(), save.getUsername());
    }

}