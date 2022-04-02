package com.devu.backend.service;

import com.devu.backend.common.exception.*;
import com.devu.backend.entity.User;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public User createUser(final String email,final String authKey) {
        User user = User.builder()
                .email(email)
                .emailAuthKey(authKey)
                .build();
        log.info("Crate New User Id : {}, Email : {}",user.getId(),user.getEmail());
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

    //회원가입용 createUser
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

    public boolean isEmailConfirmed(final String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return user.isEmailConfirm();
    }
}
