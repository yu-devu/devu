package com.devu.backend.service;

import com.devu.backend.entity.User;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(final User user) {
        if (user == null || user.getEmail() == null) {
            throw new RuntimeException("User Entity Error");
        }
        final String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists = {}", email);
        }
        log.info("Crate New User Id : {}, Email : {}",user.getId(),user.getEmail());
        return userRepository.save(user);
    }
}
