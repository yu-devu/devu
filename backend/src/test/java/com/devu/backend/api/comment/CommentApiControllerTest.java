package com.devu.backend.api.comment;

import com.devu.backend.entity.Comment;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Chat;
import com.devu.backend.repository.comment.CommentRepository;
import com.devu.backend.repository.post.PostRepository;
import com.devu.backend.repository.UserRepository;
import com.devu.backend.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CommentApiControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private CommentService commentService;
    @Autowired private PostRepository postRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CommentRepository commentRepository;
    @Autowired private CommentApiController commentApiController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentApiController(commentService, commentRepository))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("?????? ?????? - ??????")
    void createComment_success() throws Exception {
        String url = "/api/comments";
        User user = getUser();
        Chat chat = getChat(user);
        CommentCreateRequestDto requestDto = getCommentCreateRequestDto(user, chat.getId());
        String content = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 1);
        Comment comment = all.get(0);
        assertTrue(comment.getContents().equals("????????? ??????"));
    }

    private CommentCreateRequestDto getCommentCreateRequestDto(User user, Long id) {
        return CommentCreateRequestDto.builder()
                .userId(user.getId())
                .postId(id)
                .contents("????????? ??????")
                .build();
    }

    private Chat getChat(User user) {
        Chat chat = Chat.builder()
                .user(user)
                .title("????????? ?????????")
                .content("????????? ????????? ?????????.")
                .build();
        postRepository.save(chat);
        return chat;
    }

    private User getUser() {
        User user = User.builder()
                .email("test@yu.ac.kr")
                .username("test")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user);
        return user;
    }

    @Test
    @DisplayName("?????? ?????? - ??????")
    void createComment_fail() throws Exception {
        String url = "/api/comments";
        User user = getUser();
        CommentCreateRequestDto requestDto = getCommentCreateRequestDto(user, 5L);
        String content = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"???????????? ?????? ??????????????????.\"}"));
                });;

        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 0);
    }

    @Test
    @DisplayName("????????? ?????? - ??????")
    void createReComment_success() throws Exception {
        String url = "/api/reComments";
        User user = getUser();
        Chat chat = getChat(user);
        Comment comment = getComment(user, chat);
        CommentCreateRequestDto requestDto = getCommentCreateRequestDto(user, chat, comment.getGroupNum(), comment.getUser().getUsername());
        String content = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 2);
        Comment parentComment = all.get(0);
        Comment reComment = all.get(1);
        assertTrue(reComment.getContents().equals("????????? ?????????"));
        assertTrue(reComment.getGroupNum().equals(parentComment.getGroupNum()));
        assertTrue(reComment.getParent().equals(parentComment.getUser().getUsername()));
    }

    private CommentCreateRequestDto getCommentCreateRequestDto(User user, Chat chat, Long groupNum, String username) {
        return CommentCreateRequestDto.builder()
                .userId(user.getId())
                .postId(chat.getId())
                .contents("????????? ?????????")
                .group(groupNum)
                .parent(username)
                .build();
    }

    private Comment getComment(User user, Chat chat) {
        Comment comment = Comment.builder()
                .contents("????????? ??????")
                .deleted(false)
                .user(user)
                .post(chat)
                .build();
        commentRepository.save(comment);
        comment.updateGroup(comment.getId());
        return comment;
    }

    @Test
    @DisplayName("????????? ?????? - ??????")
    void createReComment_fail() throws Exception {
        String url = "/api/reComments";
        User user = getUser();
        Chat chat = getChat(user);
        CommentCreateRequestDto requestDto = getCommentCreateRequestDto(user, chat, 1L, "test");
        String content = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"?????? ????????? ???????????? ????????????.\"}"));
                });;

        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 0);
    }

    @Test
    @DisplayName("?????? ??????")
    void viewComment() throws Exception {
        User user = getUser();
        Chat chat = getChat(user);
        String url = "/api/comments/" + chat.getId() + "?page=0&size=20";
        for (int i = 0; i< 30; i++)
            getComment(user, chat);
        mockMvc = MockMvcBuilders
                .standaloneSetup(commentApiController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        MvcResult mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        JSONArray objects = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertTrue(objects.length() == 20);
        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 30);
    }

    @Test
    @DisplayName("?????? ?????? - ??????")
    void updateComment_success() throws Exception {
        User user = getUser();
        Chat chat = getChat(user);
        Comment comment = getComment(user, chat);
        CommentUpdateRequestDto requestDto = getCommentUpdateRequestDto(comment.getId());
        String url = "/api/comments/" + comment.getId();
        String content = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(patch(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Comment updatedComment = commentRepository.findById(comment.getId()).get();
        assertTrue(updatedComment.getContents().equals("?????? ??????"));
    }

    private CommentUpdateRequestDto getCommentUpdateRequestDto(Long id) {
        return CommentUpdateRequestDto.builder()
                .commentId(id)
                .contents("?????? ??????")
                .build();
    }

    @Test
    @DisplayName("?????? ?????? - ??????")
    void updateComment_fail() throws Exception {
        User user = getUser();
        Chat chat = getChat(user);
        Comment comment = getComment(user, chat);
        CommentUpdateRequestDto requestDto = getCommentUpdateRequestDto(5L);
        String url = "/api/comments/" + requestDto.getCommentId();
        String content = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(patch(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"?????? ????????? ???????????? ????????????.\"}"));
                });;

        Comment updatedComment = commentRepository.findById(comment.getId()).get();
        assertFalse(updatedComment.getContents().equals("?????? ??????"));
    }

    @Test
    @DisplayName("?????? ?????? - ??????(????????? ?????????)")
    void deleteCommentWithReComment_success() throws Exception {
        User user = getUser();
        Chat chat = getChat(user);
        Comment comment = getComment(user, chat);
        String url = "/api/comments/" + comment.getId();
        Comment reComment = Comment.builder()
                .contents("????????? ?????????")
                .groupNum(comment.getGroupNum())
                .build();
        commentRepository.save(reComment);

        mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        assertTrue(comment.isDeleted());
        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 2);
    }

    @Test
    @DisplayName("?????? ?????? - ??????(????????? ?????????)")
    void deleteComment_success() throws Exception {
        User user = getUser();
        Chat chat = getChat(user);
        Comment comment = getComment(user, chat);
        String url = "/api/comments/" + comment.getId();

        mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 0);
    }

    @Test
    @DisplayName("?????? ?????? - ??????")
    void deleteComment_fail() throws Exception {
        User user = getUser();
        Chat chat = getChat(user);
        Comment comment = getComment(user, chat);
        String url = "/api/comments/" + 2;

        mockMvc.perform(delete(url))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"?????? ????????? ???????????? ????????????.\"}"));
                });;

        assertNotNull(comment);
    }

    @Test
    @DisplayName("????????? ?????? - ??????(???????????? delete true, ???????????? ??????????????? ?????? ??????)")
    void deleteReCommentWithParentComment_success() throws Exception {
        User user = getUser();
        Chat chat = getChat(user);
        Comment comment = getComment(user, chat);
        comment.updateDeleted();
        Comment reComment = Comment.builder()
                .contents("????????? ?????????")
                .groupNum(comment.getGroupNum())
                .build();
        commentRepository.save(reComment);
        String url = "/api/reComments/" + reComment.getId();
        mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 0);
    }

    @Test
    @DisplayName("????????? ?????? - ??????(???????????? delete false)")
    void deleteReComment_success() throws Exception {
        User user = getUser();
        Chat chat = getChat(user);
        Comment comment = getComment(user, chat);
        Comment reComment = Comment.builder()
                .contents("????????? ?????????")
                .groupNum(comment.getGroupNum())
                .build();
        commentRepository.save(reComment);
        String url = "/api/reComments/" + reComment.getId();
        mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        List<Comment> all = commentRepository.findAll();
        assertTrue(all.size() == 1);
        Comment dbComment = all.get(0);
        assertTrue(dbComment.getId() == comment.getId());
    }
}