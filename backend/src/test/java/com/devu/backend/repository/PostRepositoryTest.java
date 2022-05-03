package com.devu.backend.repository;

import com.devu.backend.config.TestConfig;
import com.devu.backend.entity.Like;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Post;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Import(TestConfig.class)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    void findTop3ByOrderByHit() {
        //given
        User user1 = createUser("brido");
        User user2 = createUser("brido2");
        Chat chat1 = createChat(user1);
        hitUp(chat1,3);
        Chat chat2 = createChat(user1);
        hitUp(chat2,5);
        Chat chat3 = createChat(user1);
        hitUp(chat3,2);
        //when
        List<Chat> chats = postRepository.findTop3ChatByOrderByHitDesc().get();
        //then
        Assertions.assertThat(chats.size()).isEqualTo(3);
        Assertions.assertThat(chats.get(0).getHit()).isEqualTo(5);
        Assertions.assertThat(chats.get(1).getHit()).isEqualTo(3);
        Assertions.assertThat(chats.get(2).getHit()).isEqualTo(2);

    }
    @Test
    void findTop3ByOrderByLikes() {
        //given
        User user1 = createUser("brido");
        User user2 = createUser("brido2");
        Chat chat1 = createChat(user1);
        Chat chat2 = createChat(user1);
        Chat chat3 = createChat(user1);
        createLike(user1, chat1);
        createLike(user2, chat1);
        createLike(user1, chat2);
        //when
        List<Chat> chats = postRepository.findTop3ChatByOrderByLikes().get();
        //then
        Assertions.assertThat(chats.size()).isEqualTo(3);
        Assertions.assertThat(chats.get(0).getLikes().size()).isEqualTo(2);
        Assertions.assertThat(chats.get(1).getLikes().size()).isEqualTo(1);
        Assertions.assertThat(chats.get(2).getLikes().size()).isEqualTo(0);

    }
    private Like createLike(User user, Post post) {
        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();
        likeRepository.save(like);
        like.changePost(post);
        return like;
    }

    private Chat createChat(User user) {
        Chat chat = Chat.builder()
                .content("test-content")
                .title("test-title")
                .user(user)
                .hit(0L)
                .likes(new ArrayList<>())
                .build();
        postRepository.save(chat);
        return chat;
    }

    private User createUser(String username) {
        User user = User.builder()
                .email("hcs4125@gmail.com")
                .password("hcshcs")
                .username(username)
                .build();
        userRepository.save(user);
        return user;
    }

    private void hitUp(Post post,int num) {
        for (int i = 0; i < num; i++) {
            post.plusHit();
        }
    }

}