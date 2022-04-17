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

    @Transactional
    public User createUser(final String email,final String authKey) {
        User user = User.builder()
                .email(email)
                .emailAuthKey(authKey)
                .emailConfirm(false)
                .build();
        log.info("Crate New User Id : {}, Email : {}", user.getId(), user.getEmail());
        return userRepository.save(user);
    }

    public boolean isEmailExists(final String email) {
        return userRepository.existsByEmail(email);
    }

    public User getByEmail(final String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (!user.isEmailConfirm()) {
            log.warn("이메일 인증이 완료되지 않은 이메일입니다.");
        }
        return user;
    }

    //Dirty Checking
    @Transactional
    public void updateUserAuthKey(final String email, final String authKey) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        user.updateAuthKey(authKey);
    }

    /*
    * 극악의 확률로 동일한 확률의 AuthKey가 생성된 경우 예외처리
    * */
    public User getByAuthKey(final String authKey) {
        return userRepository.findByEmailAuthKey(authKey).orElseThrow(EmailAuthKeyNotEqualException::new);
    }

    @Transactional
    public void updateUserConfirm(final String authKey) {
        User user = userRepository.findByEmailAuthKey(authKey).orElseThrow(UserNotFoundException::new);
        user.updateEmailConfirm(true);
    }

    // 회원가입 마지막 절차 username,password 정보 기입
    // Dirty Checking으로 변경
    @Transactional
    public User updateUser(final UserDTO userDto) {
        User user = getByEmail(userDto.getEmail());
        if (!user.isEmailConfirm()) {
            throw new EmailConfirmNotCompleteException();
        }
        checkDupUsername(userDto.getUsername());
        user.updateUserInfo(userDto.getUsername(),passwordEncoder.encode(userDto.getPassword()));
        return user;
    }

    private void checkDupUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            log.error("이미 존재하는 username = {}",username);
            throw new IllegalStateException("이미 존재하는 username입니다.");
        }
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

    /*
    * 비밀번호 변경 메소드
    * */
    @Transactional
    public void changePassword(String email,String password) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        log.info("User {} 's before password was {}",user.getUsername(),user.getPassword());
        user.changePassword(passwordEncoder.encode(password));
        log.info("User {} 's password was changed to {}",user.getUsername(),user.getPassword());
    }

    /*
     * Admin 페이지에서 사용
     * */
    public List<User> getUsers() {
        return userRepository.findAll();
    }


}
