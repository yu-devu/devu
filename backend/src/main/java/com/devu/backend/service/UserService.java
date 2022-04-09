package com.devu.backend.service;


import com.devu.backend.common.exception.EmailConfirmNotCompleteException;
import com.devu.backend.common.exception.PasswordNotSameException;
import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.config.auth.token.RefreshToken;
import com.devu.backend.config.auth.token.RefreshTokenRepository;
import com.devu.backend.config.auth.token.TokenService;
import com.devu.backend.controller.user.UserDTO;
import com.devu.backend.common.exception.*;

import com.devu.backend.entity.User;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;

    public User getByAuthKey(final String authKey) {
        return userRepository.findByEmailAuthKey(authKey).orElseThrow(EmailAuthKeyNotEqualException::new);
    }

    public User getByEmail(final String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (user == null) {
            log.warn("이메일 인증이 완료되지 않은 이메일입니다.");
        }
        return user;
    }

    @Transactional
<<<<<<< HEAD
    public User createUser(final String email,final String authKey, final String username) {
=======
    public User createUser(final String email, final String authKey) {
>>>>>>> refs/remotes/origin/main
        User user = User.builder()
                .email(email)
                .emailAuthKey(authKey)
                .username(username)
                .build();
        log.info("Crate New User Id : {}, Email : {}", user.getId(), user.getEmail());
        return userRepository.save(user);
    }

    @Transactional
    public void updateUserAuthKey(final String email, final String authKey) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        user.updateAuthKey(authKey);
    }

    @Transactional
    public void updateUserConfirm(final User user) {
        user.updateEmailConfirm(user.isEmailConfirm());
    }

    // 회원가입용 createUser
    @Transactional
    public User updateUser(final User user) {
        if (user == null || user.getEmail() == null) {
            throw new RuntimeException("User Entity Error");
        }
        final String email = user.getEmail();
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException();
        }
        if (!user.isEmailConfirm()) {
            throw new EmailConfirmNotCompleteException();
        }
        return userRepository.save(user);
    }

    public boolean isEmailExists(final String email) {
        return userRepository.existsByEmail(email);
    }

    public User getByCredentials(final String email, final String password) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (!user.isEmailConfirm()) {
            throw new EmailConfirmNotCompleteException();
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordNotSameException();
        }
        return user;
    }

    @Transactional
    public UserDTO loginProcess(UserDTO userDTO, User user, HttpServletResponse response) {
        String accessToken = tokenService.createAccessToken(userDTO.getEmail());
        String refreshToken = tokenService.createRefreshToken(userDTO.getEmail());
        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.save(token);
        Cookie cookie = cookieService.createCookie("X-AUTH-REFRESH-TOKEN", refreshToken);
        response.addCookie(cookie);
        UserDTO responseUserDTO = UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .accessToken(accessToken)
                .build();
        return responseUserDTO;
    }

    public boolean isEmailConfirmed(final String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return user.isEmailConfirm();
    }

    public void findByUserName(String username) {
        if (userRepository.existsByUsername(username)) {
            log.error("이미 존재하는 username = {}",username);
            throw new IllegalStateException("이미 존재하는 username입니다.");
        }
    }
}
