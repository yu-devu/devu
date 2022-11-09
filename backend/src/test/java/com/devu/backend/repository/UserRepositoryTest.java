package com.devu.backend.repository;

import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.config.TestConfig;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.repository.post.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Import(TestConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    public void user등록() {
        //given
        final User user = User.builder()
                .username("test")
                .email("test@test.com")
                .password("hcshcs")
                .build();
        //when
        User savedUser = userRepository.save(user);
        //then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("test");
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void 유저찾기() {
        //given
        final User user = User.builder()
                .username("test")
                .email("test@test.com")
                .password("hcshcs")
                .posts(new ArrayList<>())
                .build();
        userRepository.save(user);
        //when
        User findUser = userRepository.findByUsername("test").get();
        //then
        assertThat(user.getUsername()).isEqualTo(findUser.getUsername());

    }

    @Test
    public void deleteUser() {
        //given
        final User user = User.builder()
                .username("test")
                .email("test@test.com")
                .password("hcshcs")
                .posts(new ArrayList<>())
                .build();
        User save = userRepository.save(user);
        //when
        userRepository.delete(user);
        //then
        assertThatThrownBy(() -> userRepository.findById(save.getId()).orElseThrow()).isInstanceOf(NoSuchElementException.class);
    }
}