package com.devu.backend.repository;

import com.devu.backend.config.TestConfig;
import com.devu.backend.entity.Like;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.*;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    void findAllStudyByStatus() {
        //given
        User user = createUser("brido");
        Study study1 = createStudy(user);
        Study study2 = createStudy(user);
        Study study3 = createStudy(user);
        study3.updateStatus(StudyStatus.CLOSED);
        //when
        List<Study> activeStudy = postRepository.findAllActiveStudy().get();
        List<Study> closedStudy = postRepository.findAllClosedStudy().get();
        //then
        Assertions.assertThat(closedStudy.size()).isEqualTo(1);
        Assertions.assertThat(closedStudy.get(0).getStudyStatus()).isEqualTo(StudyStatus.CLOSED);
        Assertions.assertThat(activeStudy.size()).isEqualTo(2);
        Assertions.assertThat(activeStudy.get(0).getStudyStatus()).isEqualTo(StudyStatus.ACTIVE);
        Assertions.assertThat(activeStudy.get(1).getStudyStatus()).isEqualTo(StudyStatus.ACTIVE);
    }

    @Test
    void findAllQuestionByStatus() {
        //given
        User user = createUser("brido");
        Question question1 = createQuestion(user);
        Question question2 = createQuestion(user);
        Question question3 = createQuestion(user);
        question3.updateStatus(QuestionStatus.SOLVED);
        //when
        List<Question> unsolvedQues = postRepository.findAllUnSolvedQuestion().get();
        List<Question> solvedQues = postRepository.findAllSolvedQuestion().get();
        //then
        Assertions.assertThat(unsolvedQues.size()).isEqualTo(2);
        Assertions.assertThat(solvedQues.size()).isEqualTo(1);
        Assertions.assertThat(unsolvedQues.get(0).getQuestionStatus()).isEqualTo(QuestionStatus.UNSOLVED);
        Assertions.assertThat(unsolvedQues.get(1).getQuestionStatus()).isEqualTo(QuestionStatus.UNSOLVED);
        Assertions.assertThat(solvedQues.get(0).getQuestionStatus()).isEqualTo(QuestionStatus.SOLVED);
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

    private Study createStudy(User user) {
        Study study = Study.builder()
                .content("test-content")
                .title("test-title")
                .user(user)
                .hit(0L)
                .likes(new ArrayList<>())
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