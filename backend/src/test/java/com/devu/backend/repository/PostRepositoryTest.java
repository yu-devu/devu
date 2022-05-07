package com.devu.backend.repository;

import com.devu.backend.config.TestConfig;
import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.entity.Like;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.*;
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

    @DisplayName("모든 스터디 찾기 - 좋아요순 + ACTIVE")
    @Test
    void findAllStudyByLikes() {
        //given
        User user1 = createUser("brido1");
        User user2 = createUser("brido2");
        Study study1 = createStudy(user1);
        Study study2 = createStudy(user1);
        createLike(user1, study1);
        createLike(user1, study2);
        createLike(user1, study2);
        PostSearch search = PostSearch.builder()
                .studyStatus(StudyStatus.ACTIVE)
                .order("likes")
                .build();
        //when
        List<PostResponseDto> responseDtos = postRepository.findAllStudies(Pageable.ofSize(10),search)
                .stream().map(p -> PostResponseDto.builder()
                        .id(p.getId())
                        .like(p.getLikes().size())
                        .title(p.getTitle())
                        .content(p.getContent())
                        .username(p.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
        //then
        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0).getLike()).isEqualTo(study2.getLikes().size());
        assertThat(responseDtos.get(1).getLike()).isEqualTo(study1.getLikes().size());
    }

    @DisplayName("모든 스터디 찾기 - 좋아요순 + CLOSED")
    @Test
    void findAllStudyByLikesClosed() {
        //given
        User user1 = createUser("brido1");
        User user2 = createUser("brido2");
        Study study1 = createStudy(user1);
        Study study2 = createStudy(user1);
        createLike(user1, study1);
        createLike(user1, study2);
        createLike(user1, study2);
        study1.updateStatus(StudyStatus.CLOSED);
        study2.updateStatus(StudyStatus.CLOSED);
        PostSearch search = PostSearch.builder()
                .studyStatus(StudyStatus.CLOSED)
                .order("likes")
                .build();
        //when
        List<PostResponseDto> responseDtos = postRepository.findAllStudies(Pageable.ofSize(10),search)
                .stream().map(p -> PostResponseDto.builder()
                        .id(p.getId())
                        .like(p.getLikes().size())
                        .title(p.getTitle())
                        .content(p.getContent())
                        .username(p.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
        //then
        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0).getLike()).isEqualTo(study2.getLikes().size());
        assertThat(responseDtos.get(1).getLike()).isEqualTo(study1.getLikes().size());
    }

    @DisplayName("모든 스터디 찾기 - 최신순(기본값) + ACTIVE")
    @Test
    void findAllStudyByCreatedAt() {
        //given
        User user1 = createUser("brido1");
        User user2 = createUser("brido2");
        Study study1 = createStudy(user1);
        Study study2 = createStudy(user1);
        PostSearch search = PostSearch.builder()
                .studyStatus(StudyStatus.ACTIVE)
                .sentence("test")
                .build();
        //when
        List<PostResponseDto> responseDtos = postRepository.findAllStudies(Pageable.ofSize(10),search)
                .stream().map(p -> PostResponseDto.builder()
                        .id(p.getId())
                        .like(p.getLikes().size())
                        .title(p.getTitle())
                        .content(p.getContent())
                        .username(p.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
        //then
        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0).getId()).isEqualTo(study2.getId());
        assertThat(responseDtos.get(1).getId()).isEqualTo(study1.getId());
    }

    @DisplayName("모든 스터디 찾기 - 최신순(기본값) + CLOSED")
    @Test
    void findAllStudyByCreatedAtClosed() {
        //given
        User user1 = createUser("brido1");
        User user2 = createUser("brido2");
        Study study1 = createStudy(user1);
        Study study2 = createStudy(user1);
        study1.updateStatus(StudyStatus.CLOSED);
        study2.updateStatus(StudyStatus.CLOSED);
        PostSearch search = PostSearch.builder()
                .studyStatus(StudyStatus.CLOSED)
                .build();
        //when
        List<PostResponseDto> responseDtos = postRepository.findAllStudies(Pageable.ofSize(10),search)
                .stream().map(p -> PostResponseDto.builder()
                        .id(p.getId())
                        .like(p.getLikes().size())
                        .title(p.getTitle())
                        .content(p.getContent())
                        .username(p.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
        //then
        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0).getId()).isEqualTo(study2.getId());
        assertThat(responseDtos.get(1).getId()).isEqualTo(study1.getId());
    }

    @DisplayName("모든 스터디 찾기 - 댓글순")
    @Test
    void findAllStudyByComments() {
        //given
        User user1 = createUser("brido1");
        User user2 = createUser("brido2");
        Study study1 = createStudy(user1);
        Study study2 = createStudy(user1);
        createComment(user1, study1);
        createComment(user1, study1);
        createComment(user1, study2);
        PostSearch search = PostSearch.builder()
                .studyStatus(StudyStatus.ACTIVE)
                .order("comments")
                .build();
        //when
        List<PostResponseDto> responseDtos = postRepository.findAllStudies(Pageable.ofSize(10),search)
                .stream().map(p -> PostResponseDto.builder()
                        .id(p.getId())
                        .like(p.getLikes().size())
                        .comments(p.getComments())
                        .title(p.getTitle())
                        .content(p.getContent())
                        .username(p.getUser().getUsername())
                        .build()
                ).collect(Collectors.toList());
        //then
        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0).getComments().size()).isEqualTo(study1.getComments().size());
        assertThat(responseDtos.get(1).getComments().size()).isEqualTo(study2.getComments().size());
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

    @Test
    void findAllStudyByStatus() {
        //given
        User user = createUser("brido");
        Study study1 = createStudy(user);
        Study study2 = createStudy(user);
        Study study3 = createStudy(user);
        study3.updateStatus(StudyStatus.CLOSED);
        //when
        Page<Study> activeStudy = postRepository.findAllStudyByStatus(Pageable.ofSize(10),StudyStatus.ACTIVE);
        Page<Study> closedStudy = postRepository.findAllStudyByStatus(Pageable.ofSize(10),StudyStatus.CLOSED);
        //then
        assertThat(closedStudy.getTotalElements()).isEqualTo(1);
        assertThat(closedStudy.get().collect(Collectors.toList()).get(0).getStudyStatus()).isEqualTo(StudyStatus.CLOSED);
        assertThat(activeStudy.getTotalElements()).isEqualTo(2);
        assertThat(activeStudy.get().collect(Collectors.toList()).get(0).getStudyStatus()).isEqualTo(StudyStatus.ACTIVE);
        assertThat(activeStudy.get().collect(Collectors.toList()).get(1).getStudyStatus()).isEqualTo(StudyStatus.ACTIVE);
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
        Page<Question> unsolvedQues = postRepository.findAllQuestionByStatus(Pageable.ofSize(10),QuestionStatus.UNSOLVED);
        Page<Question> solvedQues = postRepository.findAllQuestionByStatus(Pageable.ofSize(10),QuestionStatus.SOLVED);
        //then
        assertThat(unsolvedQues.getTotalElements()).isEqualTo(2);
        assertThat(solvedQues.getTotalElements()).isEqualTo(1);
        assertThat(unsolvedQues.get().collect(Collectors.toList()).get(0).getQuestionStatus()).isEqualTo(QuestionStatus.UNSOLVED);
        assertThat(unsolvedQues.get().collect(Collectors.toList()).get(1).getQuestionStatus()).isEqualTo(QuestionStatus.UNSOLVED);
        assertThat(solvedQues.get().collect(Collectors.toList()).get(0).getQuestionStatus()).isEqualTo(QuestionStatus.SOLVED);
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