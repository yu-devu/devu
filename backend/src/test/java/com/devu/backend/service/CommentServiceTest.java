package com.devu.backend.service;

import com.devu.backend.api.comment.CommentCreateRequestDto;
import com.devu.backend.api.comment.CommentUpdateRequestDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Post;
import com.devu.backend.repository.comment.CommentRepository;
import com.devu.backend.repository.post.PostRepository;
import com.devu.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;



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
        BDDMockito.given(commentRepository.save(any(Comment.class))).willReturn(comment);
        BDDMockito.given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        BDDMockito.given(postRepository.findById(chat.getId())).willReturn(Optional.of(chat));
        BDDMockito.given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        //when
        commentService.saveComment(createRequestDto);
        //then
        Comment find = commentRepository.findById(comment.getId()).get();
        assertEquals(comment.getContents(), find.getContents());
        assertEquals(comment.getUser().getId(),find.getUser().getId());
    }

    @Test
    void update() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        CommentCreateRequestDto createRequestDto = createCreateRequestDto(user, chat);
        Comment comment = createComment(user, chat, createRequestDto);
        commentRepository.save(comment);
        CommentUpdateRequestDto updateRequestDto = createUpdateRequestDto(comment);
        //Mocking
        BDDMockito.given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        //when
        commentService.updateComment(updateRequestDto);
        //then
        Comment find = commentRepository.findById(chat.getId()).get();
        assertEquals(updateRequestDto.getContents(), find.getContents());
    }

    private CommentUpdateRequestDto createUpdateRequestDto(Comment comment) {
        return CommentUpdateRequestDto.builder()
                .contents("update content")
                .commentId(comment.getId())
                .build();
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
                .username(user.getUsername())
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