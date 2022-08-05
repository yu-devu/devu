package com.devu.backend.repository;

import com.devu.backend.config.TestConfig;
import com.devu.backend.entity.*;
import com.devu.backend.entity.post.*;
import com.devu.backend.repository.comment.CommentRepository;
import com.devu.backend.repository.post.PostRepository;
import com.devu.backend.repository.post.PostSearch;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

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
        createLike(user1,chat1);
        createLike(user2, chat1);
        createLike(user1, chat2);


        createComment(user2, chat1);
        createComment(user1, chat1);
        createComment(user1, chat2);

        chat1.plusHit();


        Tag spring = createTag("SPRING");
        Tag react = createTag("REACT");
        Tag c = createTag("C");
        Tag vue = createTag("VUE");
        Study study1 = createStudy(user1);
        Study study2 = createStudy(user1);

        createLike(user2, study1);
        createLike(user2, study1);


        PostTag postTag1 = PostTag.builder().post(study1).tag(spring).build();
        PostTag postTag2 = PostTag.builder().post(study1).tag(react).build();
        PostTag postTag3 = PostTag.builder().post(study2).tag(c).build();
        PostTag postTag4 = PostTag.builder().post(study2).tag(vue).build();

        study1.getPostTags().add(postTag1);
        study1.getPostTags().add(postTag2);
        study2.getPostTags().add(postTag3);
        study2.getPostTags().add(postTag4);
    }

    @DisplayName("모든 Posts GET")
    @Test
    void findAll() {
        //given
        //when
        List<Post> posts = postRepository.findAll();
        //then
        Assertions.assertThat(posts.size()).isEqualTo(4);
    }

    @DisplayName("최신순(디폴트) 정렬 테스트")
    @Test
    void findChatsWithDate() {
        //given
        User user = createUser("test");
        Chat chat = createChat(user);
        //when
        List<Chat> chats = postRepository.findAllChats(PageRequest.of(0,10), PostSearch.builder().build()).stream().collect(Collectors.toList());
        //then
        assertThat(chats.size()).isEqualTo(3);
        assertThat(chats.get(0).getUser().getUsername()).isEqualTo("test");
    }

    @DisplayName("좋아요순 정렬 테스트")
    @Test
    void findChatsWithLikes() {
        //given
        User user = createUser("test");
        Chat chat = createChat(user);
        //when
        List<Chat> chats = postRepository.findAllChats(PageRequest.of(0,10),
                PostSearch.builder()
                        .order("likes")
                .build()).stream().collect(Collectors.toList());
        //then
        assertThat(chats.size()).isEqualTo(3);
        assertThat(chats.get(0).getLikes().size()).isGreaterThan(chats.get(1).getLikes().size());
        assertThat(chats.get(1).getLikes().size()).isGreaterThan(chats.get(2).getLikes().size());
    }
    @DisplayName("댓글순 정렬 테스트")
    @Test
    void findChatsWithComments() {
        //given
        User user = createUser("test");
        Chat chat = createChat(user);
        //when
        List<Chat> chats = postRepository.findAllChats(PageRequest.of(0,10),
                PostSearch.builder()
                        .order("comments")
                        .build()).stream().collect(Collectors.toList());
        //then
        assertThat(chats.size()).isEqualTo(3);
        assertThat(chats.get(0).getComments().size()).isGreaterThan(chats.get(1).getLikes().size());
        assertThat(chats.get(1).getComments().size()).isGreaterThan(chats.get(2).getLikes().size());
    }

    @DisplayName("검색어 정렬 테스트 - 성공")
    @Test
    void findChatsWithSearchingString() {
        //given
        User user = createUser("test");
        Chat chat = createChat(user);
        //when
        List<Chat> chats = postRepository.findAllChats(PageRequest.of(0,10),
                PostSearch.builder()
                        .sentence("test")
                        .build()).stream().collect(Collectors.toList());
        //then
        assertThat(chats.size()).isEqualTo(3);
    }

    @DisplayName("검색어 정렬 테스트 - 실패")
    @Test
    void findChatsWithSearchingStringFail() {
        //given
        User user = createUser("test");
        Chat chat = createChat(user);
        //when
        List<Chat> chats = postRepository.findAllChats(PageRequest.of(0,10),
                PostSearch.builder()
                        .sentence("hello")
                        .build()).stream().collect(Collectors.toList());
        //then
        assertThat(chats.size()).isEqualTo(0);
    }

    //ACTIVE 2, CLOSED 1
    @DisplayName("study 상태 필터링 테스트 - ACTIVE")
    @Test
    void studyActiveStatusTest() {
        //given
        User user = createUser("test");
        Study study = createStudy(user);
        Tag vue = tagRepository.findTagByName("VUE").get();
        PostTag postTag = PostTag.builder().post(study).tag(vue).build();
        study.getPostTags().add(postTag);
        study.updateStatus(StudyStatus.CLOSED);

        //when
        List<Study> studies = postRepository.findAllStudies(PageRequest.of(0, 10),
                PostSearch.builder()
                        .tagId(new ArrayList<>())
                        .studyStatus(StudyStatus.ACTIVE)
                        .build()
        ).stream().collect(Collectors.toList());
        //then
        assertThat(studies.size()).isEqualTo(2);
        assertThat(studies.get(0).getStudyStatus()).isEqualTo(StudyStatus.ACTIVE);
        assertThat(studies.get(1).getStudyStatus()).isEqualTo(StudyStatus.ACTIVE);
    }

    @DisplayName("study 상태 필터링 테스트 - CLOSED")
    @Test
    void studyClosedStatusTest() {
        //given
        User user = createUser("test");
        Study study = createStudy(user);
        Tag vue = tagRepository.findTagByName("VUE").get();
        PostTag postTag = PostTag.builder().post(study).tag(vue).build();
        study.getPostTags().add(postTag);
        study.updateStatus(StudyStatus.CLOSED);

        //when
        List<Study> studies = postRepository.findAllStudies(PageRequest.of(0, 10),
                PostSearch.builder()
                        .tagId(new ArrayList<>())
                        .studyStatus(StudyStatus.CLOSED)
                        .build()
        ).stream().collect(Collectors.toList());
        //then
        assertThat(studies.size()).isEqualTo(1);
        assertThat(studies.get(0).getStudyStatus()).isEqualTo(StudyStatus.CLOSED);
    }

    @Test
    void tagFilteringTest() {
        //given
        User user = createUser("test");
        Study study = createStudy(user);
        Tag vue = tagRepository.findTagByName("VUE").get();
        Tag spring = tagRepository.findTagByName("SPRING").get();
        PostTag postTag = PostTag.builder().post(study).tag(vue).build();
        PostTag postTag2 = PostTag.builder().post(study).tag(spring).build();
        study.getPostTags().add(postTag);
        study.getPostTags().add(postTag2);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(vue.getId());
        //when
        List<Study> vueStudies = postRepository.findAllStudies(PageRequest.of(0, 10),
                PostSearch.builder()
                        .tagId(ids)
                        .build()
        ).stream().collect(Collectors.toList());
        //then
        assertThat(vueStudies.size()).isEqualTo(2);
    }

    @DisplayName("마이페이지 - 내가쓴 Chat 게시글")
    @Test
    void findAllMyChats() {
        //given
        User daebak = userRepository.findByUsername("daebak").get();
        createChat(daebak);
        //when
        List<Chat> chats = postRepository.findAllChatsByUser(daebak).get();
        //then
        assertThat(chats.size()).isEqualTo(2);
        assertThat(chats.get(0).getUser().getUsername()).isEqualTo(daebak.getUsername());
        assertThat(chats.get(1).getUser().getUsername()).isEqualTo(daebak.getUsername());
    }

    @DisplayName("마이페이지 - 내가쓴 스터디 게시글")
    @Test
    void findAllMyStudies() {
        //given
        User brido = userRepository.findByUsername("brido").get();
        //when
        List<Study> studies = postRepository.findAllStudiesByUser(brido).get();
        //then
        assertThat(studies.size()).isEqualTo(2);
        assertThat(studies.get(0).getUser().getUsername()).isEqualTo(brido.getUsername());
        assertThat(studies.get(1).getUser().getUsername()).isEqualTo(brido.getUsername());
    }

    @DisplayName("마이페이지 - 내가 쓴 질문 게시글 생성하지 않을 경우 테스트")
    @Test
    void findAllMyQuestions() {
        //given
        User brido = userRepository.findByUsername("brido").get();
        //when
        List<Question> questions = postRepository.findAllQuestionsByUser(brido).get();
        //then
        assertThat(questions.size()).isEqualTo(0);
    }

    @DisplayName("마이페이지 - 내가 좋아요한 chat 불러오기")
    @Test
    void findAllLikeChats() {
        //given
        User brido = userRepository.findByUsername("brido").get();
        User daebak = userRepository.findByUsername("daebak").get();
        //when
        List<Chat> chats1 = postRepository.findAllLikeChatsByUserId(brido.getId()).get();
        List<Chat> chats2 = postRepository.findAllLikeChatsByUserId(daebak.getId()).get();
        //then
        assertThat(chats1.size()).isEqualTo(2);
        assertThat(chats2.size()).isEqualTo(1);
    }


    @DisplayName("마이페이지 - 내가 좋아요한 스터디 게시글 불러오기")
    @Test
    void findAllLikeStudies() {
        //given
        User daebak = userRepository.findByUsername("daebak").get();
        //when
        List<Study> studies = postRepository.findAllLikeStudiesByUserId(daebak.getId()).get();
        //then
        assertThat(studies.size()).isEqualTo(2);
        assertThat(studies.get(0).getLikes().get(0).getUser().getUsername()).isEqualTo(daebak.getUsername());
    }

    @DisplayName("마이페이지 - 내가 좋아요한 질문 게시글 불러오기 - 없는 경우")
    @Test
    void findAllLikeQuestions() {
        //given
        User brido = userRepository.findByUsername("brido").get();
        //when
        List<Question> questions = postRepository.findAllLikeQuestionsByUserId(brido.getId()).get();
        //then
        assertThat(questions.size()).isEqualTo(0);
    }

    @Test
    void findTop3ByOrderByHit() {
        //given
        User user = createUser("test");
        Chat chat = createChat(user);
        hitUp(chat,3);
        //when
        List<Chat> chats = postRepository.findTop3ChatByOrderByHitDesc().get();
        //then
        assertThat(chats.size()).isEqualTo(3);
        assertThat(chats.get(0).getHit()).isEqualTo(3);
        assertThat(chats.get(1).getHit()).isEqualTo(1);
        assertThat(chats.get(2).getHit()).isEqualTo(0);

    }
    @Test
    void findTop3ByOrderByLikes() {
        //given
        User user = createUser("test");
        Chat chat = createChat(user);
        //when
        List<Chat> chats = postRepository.findTop3ChatByOrderByLikes().get();
        //then
        assertThat(chats.size()).isEqualTo(3);
        assertThat(chats.get(0).getLikes().size()).isEqualTo(2);
        assertThat(chats.get(1).getLikes().size()).isEqualTo(1);
        assertThat(chats.get(2).getLikes().size()).isEqualTo(0);

    }

    @Test
    void findAllPostsByUser() {
        //given
        User user = createUser("test");
        Chat chat1 = createChat(user);
        Chat chat2 = createChat(user);
        //when
        List<Post> posts = postRepository.findAllByUserId(user.getId()).get();
        //then
        assertThat(posts.size()).isEqualTo(2);
    }

    private Tag createTag(String name) {
        Tag tag = Tag.builder()
                .name(name)
                .build();
        tagRepository.save(tag);
        return tag;
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