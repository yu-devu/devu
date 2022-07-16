package com.devu.backend.config.auth;

import com.devu.backend.entity.User;
import com.devu.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("인증 객체 반환")
    void loadUserByUsername() {
        String test = "test@gmail.com";
        User user = User.builder()
                .email(test)
                .build();
        given(userRepository.findByEmail(test)).willReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(test);

        assertEquals(userDetails.getUsername(), test);
    }
}