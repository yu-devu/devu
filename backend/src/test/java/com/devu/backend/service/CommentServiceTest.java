package com.devu.backend.service;

import com.devu.backend.controller.comment.CommentCreateRequestDto;
import com.devu.backend.controller.comment.CommentResponseDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Post;
import com.devu.backend.repository.CommentRepository;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @DisplayName("CommentSave - Success")
    @Test
    void save() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        CommentCreateRequestDto createRequestDto = createCreateRequestDto(user, chat);
        Comment comment = createComment(user, chat, createRequestDto);
        //Mocking
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        //when
        commentService.saveComment(createRequestDto);
        //then
        Comment find = commentRepository.findById(comment.getId()).get();
        assertEquals(comment.getContents(), find.getContents());
        assertEquals(comment.getUser().getId(),find.getUser().getId());
    }

    @Test
    void saveFail() {
    }

    @Test
    void update() {

    }

    private Comment createComment(User user, Post post, CommentCreateRequestDto dto) {
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .contents(dto.getContents())
                .build();
        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(comment, "id", fakeUserId);
        return comment;
    }



    private CommentCreateRequestDto createCreateRequestDto(User user,Chat chat) {
        return CommentCreateRequestDto.builder()
                .postId(chat.getId())
                .userId(user.getId())
                .contents("hi hello")
                .build();
    }

    private User createUser() {
        String email = "test@test.com";
        User user = User.builder()
                .email(email)
                .username("test")
                .emailConfirm(false)
                .build();
        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(user, "id", fakeUserId);
        return user;
    }

    private Chat createChat(User user) {
        Chat chat = Chat.builder()
                .content("test")
                .user(user)
                .build();
        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(chat, "id", fakeUserId);
        return chat;
    }

}