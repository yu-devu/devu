package com.devu.backend.repository;

import com.devu.backend.config.TestConfig;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestConfig.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

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
    public void deleteUser() {
        //given
        final User user = User.builder()
                .username("test")
                .email("test@test.com")
                .password("hcshcs")
                .posts(new ArrayList<>())
                .build();
        Chat chat = Chat.builder()
                .title("hi")
                .content("bye")
                .user(user)
                .build();
        userRepository.save(user);
        postRepository.save(chat);
        user.addPost(chat);
        //when
        userRepository.delete(user);
        user.getPosts().clear();
        //then
        assertThat(userRepository.findAll().size()).isEqualTo(0);
        assertThat(postRepository.findAll().size()).isEqualTo(0);
    }
}