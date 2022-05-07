package com.devu.backend.service;

import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.entity.Tag;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Post;
import com.devu.backend.entity.post.PostTags;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostRepository postRepository;


    @Test
    void tagFindTest() {
        User user = createUser();
        Chat chat = createChat(user);
        Tag springTag = createSpringTag(chat);
        PostTags postTags = PostTags.valueOf("SPRING");
        assertThat(postTags).isEqualTo(springTag.getPostTags());
    }

    @Test
    void getTagsByPostId() {
        //given
    }

    private List<Tag> createTagList(Tag springTag, Tag reactTag) {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(springTag);
        tagList.add(reactTag);
        return tagList;
    }

    private List<String> createStringList(Tag springTag, Tag reactTag) {
        List<String> tags = new ArrayList<>();
        tags.add(springTag.getPostTags().title());
        tags.add(reactTag.getPostTags().title());
        return tags;
    }

    private User createUser() {
        User user = User.builder()
                .username("test")
                .email("test@test.com")
                .emailConfirm(true)
                .emailAuthKey("test")
                .password("hcshcs")
                .build();
        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(user, "id", fakeUserId);
        return user;
    }

    private Chat createChat(User user) {
        Chat chat = Chat.builder()
                .hit(0L)
                .content("test")
                .user(user)
                .tags(new ArrayList<>())
                .build();
        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(chat, "id", fakeUserId);
        return chat;
    }

    private Tag createSpringTag(Post post) {
        Tag tag = Tag.builder()
                .postTags(PostTags.SPRING)
                .post(post)
                .build();
        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(tag, "id", fakeUserId);
        return tag;
    }

    private Tag createReactTag(Post post) {
        Tag tag = Tag.builder()
                .postTags(PostTags.REACT)
                .post(post)
                .build();
        Long fakeUserId = 2L;
        ReflectionTestUtils.setField(tag, "id", fakeUserId);
        return tag;
    }
}