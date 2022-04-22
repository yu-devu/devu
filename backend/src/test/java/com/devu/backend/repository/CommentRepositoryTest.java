package com.devu.backend.repository;

import com.devu.backend.entity.Comment;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;



    @Test
    void save() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        Comment comment = createComment(user, chat);
        //when
        Comment save = commentRepository.save(comment);
        //then
        assertThat("hi").isEqualTo(save.getContents());
        assertThat(chat.getId()).isEqualTo(save.getPost().getId());
        assertThat(user.getId()).isEqualTo(save.getUser().getId());
    }

    @Test
    void findById() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        Comment comment = createComment(user, chat);
        commentRepository.save(comment);
        //when
        Comment find = commentRepository.findById(comment.getId()).get();
        //then
        assertThat(comment.getId()).isEqualTo(find.getId());
    }

    @Test
    void update() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        Comment comment = createComment(user, chat);
        commentRepository.save(comment);
        //when
        Comment find = commentRepository.findById(comment.getId()).get();
        comment.updateContent("update");
        //then
        assertThat("update").isEqualTo(find.getContents());
    }

    @Test
    void findAllByPostIdOrderByCreateAt() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        Comment comment1 = createComment(user, chat);
        Comment comment2 = createComment(user, chat);
        Comment comment3 = createComment(user, chat);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        //when
        List<Comment> list = commentRepository.findAllByPostIdOrderByCreateAt(chat.getId());
        //then
        assertThat(3).isEqualTo(list.size());
        assertThat(list.get(0)).isEqualTo(comment1);
        assertThat(list.get(1)).isEqualTo(comment2);
        assertThat(list.get(2)).isEqualTo(comment3);
    }

    @Test
    void delete() {
        //given
        User user = createUser();
        Chat chat = createChat(user);
        Comment comment = createComment(user, chat);
        commentRepository.save(comment);
        //when
        commentRepository.deleteById(comment.getId());
        List<Comment> list = commentRepository.findAll();
        //then
        assertThat(list.size()).isEqualTo(0);
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

    private Comment createComment(User user, Chat chat) {
        return Comment.builder()
                .post(chat)
                .user(user)
                .contents("hi")
                .build();
    }

    private Chat createChat(User user) {
        return Chat.builder()
                .hit(0L)
                .content("test")
                .user(user)
                .build();
    }


}