package com.devu.backend.repository;

import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.entity.Tag;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Post;
import com.devu.backend.entity.post.PostTags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        Tag springTag = createSpringTag(chat);
        Tag jsTag = createJSTag(chat);
        //when
        Tag save1 = tagRepository.save(springTag);
        Tag save2 = tagRepository.save(jsTag);
        //then
        assertThat(springTag.getPostTags()).isEqualTo(save1.getPostTags());
        assertThat(jsTag.getPostTags()).isEqualTo(save2.getPostTags());
    }

    @Test
    void update() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        Tag springTag = createSpringTag(chat);
        tagRepository.save(springTag);
        PostTags updateTag = PostTags.NODEJS;
        //when
        Tag tag = tagRepository.findById(springTag.getId()).get();//IDENTITY라서 query가 commit하기 전에 먼저 나가서 가능
        tag.updateTag(updateTag);
        //then
        assertThat(updateTag).isEqualTo(tag.getPostTags());
    }

    @Test
    void delete() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        Tag springTag = createSpringTag(chat);
        tagRepository.save(springTag);
        //when
        tagRepository.deleteById(springTag.getId());
        //then
        assertThat(tagRepository.findAll().size()).isEqualTo(0);
    }

    private User createUser() {
        return User.builder()
                .username("test")
                .email("test@test.com")
                .emailConfirm(true)
                .emailAuthKey("test")
                .password("hcshcs")
                .build();
    }

    private Chat createChat(User user) {
        return Chat.builder()
                .hit(0L)
                .content("test")
                .user(user)
                .build();
    }

    private Tag createSpringTag(Post post) {
        return Tag.builder()
                .postTags(PostTags.SPRING)
                .post(post)
                .build();
    }

    private Tag createJSTag(Post post) {
        return Tag.builder()
                .postTags(PostTags.JS)
                .post(post)
                .build();
    }
}