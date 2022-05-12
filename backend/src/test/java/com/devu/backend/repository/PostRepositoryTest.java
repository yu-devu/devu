package com.devu.backend.repository;

import com.devu.backend.config.TestConfig;
import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.entity.Like;
import com.devu.backend.entity.Tag;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        User user1 = createUser("brido");
        User user2 = createUser("daebak");
        Chat chat1 = createChat(user1);
        Chat chat2 = createChat(user2);
        Tag spring = createTag("SPRING");
        Tag nodejs = createTag("NODEJS");
        Tag react = createTag("REACT");
        Tag vue = createTag("VUE");
        Study study1 = createStudy(user1);
        Study study2 = createStudy(user2);
        Question question1 = createQuestion(user1);
        Question question2 = createQuestion(user2);
        createLike(user1,chat1);
        createLike(user1, study1);
        createLike(user1, question1);
        createComment(user2, chat2);
        createComment(user2, study2);
        createComment(user2, question2);

    }

    @DisplayName("모든 Posts GET")
    @Test
    void findAll() {
        //given
        //when
        List<Post> posts = postRepository.findAll();
        //then
        Assertions.assertThat(posts.size()).isEqualTo(6);
    }

    @Test
    void findAllChats() {
        //given
        //when
        //then
    }

    @Test
    void findAllStudies() {
        //given
        //when
        //then
    }

    @Test
    void findAllQuestions() {
        //given
        //when
        //then
    }

    @Test
    void findAllMyChats() {
        //given
        //when
        //then
    }

    @Test
    void findAllMyStudies() {
        //given
        //when
        //then
    }

    @Test
    void findAllMyQuestions() {
        //given
        //when
        //then
    }

    @Test
    void findAllLikeChats() {
        //given
        //when
        //then
    }

    @Test
    void findAllLikeQuestions() {
        //given
        //when
        //then
    }

    @Test
    void findAllLikeStudies() {
        //given
        //when
        //then
    }


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
        assertThat(chats.size()).isEqualTo(3);
        assertThat(chats.get(0).getHit()).isEqualTo(5);
        assertThat(chats.get(1).getHit()).isEqualTo(3);
        assertThat(chats.get(2).getHit()).isEqualTo(2);

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
        assertThat(chats.size()).isEqualTo(3);
        assertThat(chats.get(0).getLikes().size()).isEqualTo(2);
        assertThat(chats.get(1).getLikes().size()).isEqualTo(1);
        assertThat(chats.get(2).getLikes().size()).isEqualTo(0);

    }

    private Tag createTag(String name) {
        return Tag.builder()
                .name(name)
                .build();
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

    private Comment createComment(User user, Post post) {
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .contents("test").build();
        commentRepository.save(comment);
        comment.changePost(post);
        return comment;
    }

    private Chat createChat(User user) {
        Chat chat = Chat.builder()
                .content("test-content")
                .title("test-title")
                .user(user)
                .hit(0L)
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
        postRepository.save(chat);
        return chat;
    }

    private Study createStudy(User user) {
        Study study = Study.builder()
                .content("test-content")
                .title("test-title")
                .user(user)
                .hit(0L)
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .tags(new ArrayList<>())
                .studyStatus(StudyStatus.ACTIVE)
                .build();
        postRepository.save(study);
        return study;
    }

    private Question createQuestion(User user) {
        Question question = Question.builder()
                .content("test-content")
                .title("test-title")
                .user(user)
                .hit(0L)
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .qnaStatus(QuestionStatus.UNSOLVED)
                .build();
        postRepository.save(question);
        return question;
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