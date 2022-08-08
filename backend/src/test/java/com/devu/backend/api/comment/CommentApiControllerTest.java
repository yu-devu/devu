//package com.devu.backend.api.comment;
//
//import com.devu.backend.entity.Comment;
//import com.devu.backend.entity.User;
//import com.devu.backend.entity.post.Chat;
//import com.devu.backend.repository.comment.CommentRepository;
//import com.devu.backend.repository.post.PostRepository;
//import com.devu.backend.repository.UserRepository;
//import com.devu.backend.service.CommentService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.JSONArray;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Transactional
//@SpringBootTest
//@AutoConfigureMockMvc
//class CommentApiControllerTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private ObjectMapper objectMapper;
//    @Autowired private UserRepository userRepository;
//    @Autowired private CommentService commentService;
//    @Autowired private PostRepository postRepository;
//    @Autowired private PasswordEncoder passwordEncoder;
//    @Autowired private CommentRepository commentRepository;
//    @Autowired private CommentApiController commentApiController;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new CommentApiController(commentService, commentRepository))
//                .addFilters(new CharacterEncodingFilter("UTF-8", true))
//                .build();
//    }
//
//    @Test
//    @DisplayName("댓글 생성 - 성공")
//    void createComment_success() throws Exception {
//        String url = "/api/comments";
//        User user = getUser();
//        Chat chat = getChat(user);
//        CommentCreateRequestDto requestDto = getCommentCreateRequestDto(user, chat.getId());
//        String content = objectMapper.writeValueAsString(requestDto);
//
//        mockMvc.perform(post(url)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 1);
//        Comment comment = all.get(0);
//        assertTrue(comment.getContents().equals("테스트 댓글"));
//    }
//
//    private CommentCreateRequestDto getCommentCreateRequestDto(User user, Long id) {
//        return CommentCreateRequestDto.builder()
//                .username(user.getUsername())
//                .postId(id)
//                .contents("테스트 댓글")
//                .build();
//    }
//
//    private Chat getChat(User user) {
//        Chat chat = Chat.builder()
//                .user(user)
//                .title("테스트 게시글")
//                .content("테스트 게시글 입니다.")
//                .build();
//        postRepository.save(chat);
//        return chat;
//    }
//
//    private User getUser() {
//        User user = User.builder()
//                .email("test@yu.ac.kr")
//                .username("test")
//                .password(passwordEncoder.encode("1234"))
//                .build();
//        userRepository.save(user);
//        return user;
//    }
//
//    @Test
//    @DisplayName("댓글 생성 - 실패")
//    void createComment_fail() throws Exception {
//        String url = "/api/comments";
//        User user = getUser();
//        CommentCreateRequestDto requestDto = getCommentCreateRequestDto(user, 5L);
//        String content = objectMapper.writeValueAsString(requestDto);
//
//        mockMvc.perform(post(url)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> {
//                    MockHttpServletResponse response = result.getResponse();
//                    assertTrue(response.getContentAsString().equals("{\"error\":\"존재하지 않는 게시글입니다.\"}"));
//                });;
//
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 0);
//    }
//
//    @Test
//    @DisplayName("대댓글 생성 - 성공")
//    void createReComment_success() throws Exception {
//        String url = "/api/reComments";
//        User user = getUser();
//        Chat chat = getChat(user);
//        Comment comment = getComment(user, chat);
//        CommentCreateRequestDto requestDto = getCommentCreateRequestDto(user, chat, comment.getGroupNum(), comment.getUser().getUsername());
//        String content = objectMapper.writeValueAsString(requestDto);
//
//        mockMvc.perform(post(url)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 2);
//        Comment parentComment = all.get(0);
//        Comment reComment = all.get(1);
//        assertTrue(reComment.getContents().equals("테스트 대댓글"));
//        assertTrue(reComment.getGroupNum().equals(parentComment.getGroupNum()));
//        assertTrue(reComment.getParent().equals(parentComment.getUser().getUsername()));
//    }
//
//    private CommentCreateRequestDto getCommentCreateRequestDto(User user, Chat chat, Long groupNum, String username) {
//        return CommentCreateRequestDto.builder()
//                .username(user.getUsername())
//                .postId(chat.getId())
//                .contents("테스트 대댓글")
//                .group(groupNum)
//                .parent(username)
//                .build();
//    }
//
//    private Comment getComment(User user, Chat chat) {
//        Comment comment = Comment.builder()
//                .contents("테스트 댓글")
//                .deleted(false)
//                .user(user)
//                .post(chat)
//                .build();
//        commentRepository.save(comment);
//        comment.updateGroup(comment.getId());
//        return comment;
//    }
//
//    @Test
//    @DisplayName("대댓글 생성 - 실패")
//    void createReComment_fail() throws Exception {
//        String url = "/api/reComments";
//        User user = getUser();
//        Chat chat = getChat(user);
//        CommentCreateRequestDto requestDto = getCommentCreateRequestDto(user, chat, 1L, "test");
//        String content = objectMapper.writeValueAsString(requestDto);
//
//        mockMvc.perform(post(url)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> {
//                    MockHttpServletResponse response = result.getResponse();
//                    assertTrue(response.getContentAsString().equals("{\"error\":\"부모 댓글이 존재하지 않습니다.\"}"));
//                });;
//
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 0);
//    }
//
//    @Test
//    @DisplayName("댓글 조회")
//    void viewComment() throws Exception {
//        User user = getUser();
//        Chat chat = getChat(user);
//        String url = "/api/comments/" + chat.getId() + "?page=0&size=20";
//        for (int i = 0; i< 30; i++)
//            getComment(user, chat);
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(commentApiController)
//                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
//                .build();
//        MvcResult mvcResult = mockMvc.perform(get(url))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        JSONArray objects = new JSONArray(mvcResult.getResponse().getContentAsString());
//        assertTrue(objects.length() == 20);
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 30);
//    }
//
//    @Test
//    @DisplayName("댓글 수정 - 성공")
//    void updateComment_success() throws Exception {
//        User user = getUser();
//        Chat chat = getChat(user);
//        Comment comment = getComment(user, chat);
//        CommentUpdateRequestDto requestDto = getCommentUpdateRequestDto(comment.getId());
//        String url = "/api/comments/" + comment.getId();
//        String content = objectMapper.writeValueAsString(requestDto);
//
//        mockMvc.perform(patch(url)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        Comment updatedComment = commentRepository.findById(comment.getId()).get();
//        assertTrue(updatedComment.getContents().equals("댓글 수정"));
//    }
//
//    private CommentUpdateRequestDto getCommentUpdateRequestDto(Long id) {
//        return CommentUpdateRequestDto.builder()
//                .commentId(id)
//                .contents("댓글 수정")
//                .build();
//    }
//
//    @Test
//    @DisplayName("댓글 수정 - 실패")
//    void updateComment_fail() throws Exception {
//        User user = getUser();
//        Chat chat = getChat(user);
//        Comment comment = getComment(user, chat);
//        CommentUpdateRequestDto requestDto = getCommentUpdateRequestDto(5L);
//        String url = "/api/comments/" + requestDto.getCommentId();
//        String content = objectMapper.writeValueAsString(requestDto);
//
//        mockMvc.perform(patch(url)
//                .content(content)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> {
//                    MockHttpServletResponse response = result.getResponse();
//                    assertTrue(response.getContentAsString().equals("{\"error\":\"해당 댓글은 존재하지 않습니다.\"}"));
//                });;
//
//        Comment updatedComment = commentRepository.findById(comment.getId()).get();
//        assertFalse(updatedComment.getContents().equals("댓글 수정"));
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 - 성공(대댓글 있을때)")
//    void deleteCommentWithReComment_success() throws Exception {
//        User user = getUser();
//        Chat chat = getChat(user);
//        Comment comment = getComment(user, chat);
//        String url = "/api/comments/" + comment.getId();
//        Comment reComment = Comment.builder()
//                .contents("테스트 대댓글")
//                .groupNum(comment.getGroupNum())
//                .build();
//        commentRepository.save(reComment);
//
//        mockMvc.perform(delete(url))
//                .andExpect(status().isOk());
//
//        assertTrue(comment.isDeleted());
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 2);
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 - 성공(대댓글 없을때)")
//    void deleteComment_success() throws Exception {
//        User user = getUser();
//        Chat chat = getChat(user);
//        Comment comment = getComment(user, chat);
//        String url = "/api/comments/" + comment.getId();
//
//        mockMvc.perform(delete(url))
//                .andExpect(status().isOk());
//
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 0);
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 - 실패")
//    void deleteComment_fail() throws Exception {
//        User user = getUser();
//        Chat chat = getChat(user);
//        Comment comment = getComment(user, chat);
//        String url = "/api/comments/" + 2;
//
//        mockMvc.perform(delete(url))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> {
//                    MockHttpServletResponse response = result.getResponse();
//                    assertTrue(response.getContentAsString().equals("{\"error\":\"해당 댓글은 존재하지 않습니다.\"}"));
//                });;
//
//        assertNotNull(comment);
//    }
//
//    @Test
//    @DisplayName("대댓글 삭제 - 성공(부모댓글 delete true, 대댓글은 자기자신만 있는 경우)")
//    void deleteReCommentWithParentComment_success() throws Exception {
//        User user = getUser();
//        Chat chat = getChat(user);
//        Comment comment = getComment(user, chat);
//        comment.updateDeleted();
//        Comment reComment = Comment.builder()
//                .contents("테스트 대댓글")
//                .groupNum(comment.getGroupNum())
//                .build();
//        commentRepository.save(reComment);
//        String url = "/api/reComments/" + reComment.getId();
//        mockMvc.perform(delete(url))
//                .andExpect(status().isOk());
//
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 0);
//    }
//
//    @Test
//    @DisplayName("대댓글 삭제 - 성공(부모댓글 delete false)")
//    void deleteReComment_success() throws Exception {
//        User user = getUser();
//        Chat chat = getChat(user);
//        Comment comment = getComment(user, chat);
//        Comment reComment = Comment.builder()
//                .contents("테스트 대댓글")
//                .groupNum(comment.getGroupNum())
//                .build();
//        commentRepository.save(reComment);
//        String url = "/api/reComments/" + reComment.getId();
//        mockMvc.perform(delete(url))
//                .andExpect(status().isOk());
//
//        List<Comment> all = commentRepository.findAll();
//        assertTrue(all.size() == 1);
//        Comment dbComment = all.get(0);
//        assertTrue(dbComment.getId() == comment.getId());
//    }
//}