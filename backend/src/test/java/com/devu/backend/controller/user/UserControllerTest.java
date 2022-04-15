package com.devu.backend.controller.user;

import com.devu.backend.entity.User;
import com.devu.backend.repository.UserRepository;
import com.devu.backend.service.EmailService;
import com.devu.backend.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired WebApplicationContext context;

    @BeforeEach public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, emailService, passwordEncoder))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("이메일 전송 - 성공")
    void sendEmail_success() throws Exception {
        String url = "/email";
        String email = "test@yu.ac.kr";

        ResultActions actions = mockMvc.perform(post(url)
                .param("email", email));

        User user = userRepository.findByEmail(email).orElseThrow();
        actions
                .andExpect(status().isOk());
        assertNotNull(user);
        assertNotNull(user.getEmailAuthKey());
        assertFalse(user.isEmailConfirm());
    }

    @Test
    @DisplayName("이메일 전송 - 실패")
    void sendEmail_fail() throws Exception {
        String url = "/email";
        String email = "test@yu.ac.kr";

        User user = userService.createUser(email, "test-key", "test");
        user.updateEmailConfirm(true);
        ResultActions actions = mockMvc.perform(post(url)
                .param("email", email));

        actions
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"이미 가입 완료된 이메일입니다.\"}"));
                });
    }

    @Test
    @DisplayName("이메일 재 전송")
    void resendEmail() throws Exception {
        String url = "/email";
        String email = "test@yu.ac.kr";

        userService.createUser(email, "test-key", "test");
        ResultActions actions = mockMvc.perform(post(url)
                .param("email", email));

        actions
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println(response.getContentAsString());
                    assertTrue(response.getContentAsString().equals("{\"error\":\"이메일 재전송 완료\"}"));
                });
        User user = userRepository.findByEmail(email).orElseThrow();

        assertFalse("test-key".equals(user.getEmailAuthKey()));
    }

    @Test
    @DisplayName("이메일 인증 - 성공")
    void checkEmail_success() throws Exception {
        String url = "/key";
        String email = "test@yu.ac.kr";
        User savedUser = createUser(email);

        ResultActions actions = mockMvc.perform(post(url)
                .param("postKey", savedUser.getEmailAuthKey()));
        actions
                .andExpect(status().isOk());
        assertTrue(savedUser.isEmailConfirm());
    }

    @Test
    @DisplayName("이메일 인증 - 실패")
    void checkEmail_fail() throws Exception {
        String url = "/key";
        String email = "test@yu.ac.kr";
        User savedUser = createUser(email);

        ResultActions actions = mockMvc.perform(post(url)
                .param("postKey", "asdasd"));

        actions
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"입력한 인증키가 일치하지 않습니다.\"}"));
                });
        assertFalse(savedUser.isEmailConfirm());
    }

    private User createUser(String email) {
        String authKey = emailService.createKey();
        User user = User.builder()
                .email(email)
                .emailAuthKey(authKey)
                .emailConfirm(false)
                .build();
        User savedUser = userService.createUser(user.getEmail(), user.getEmailAuthKey(), "");
        return savedUser;
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void register_success() throws Exception {
        String url = "/signup";
        String email = "test@yu.ac.kr";
        String username = "testUser";
        String password = "1234";
        User beforeRegister = createUser(email);
        beforeRegister.updateEmailConfirm(true);
        String content = createUserDto(email, username, password);

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isOk());
        User user = userRepository.findByEmail(email).orElseThrow();
        assertTrue(user.getUsername().equals("testUser"));
        assertTrue(passwordEncoder.matches("1234", user.getPassword()));
    }

    private String createUserDto(String email, String username, String password) throws JsonProcessingException {
        UserDTO userDTO = UserDTO.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();
        return objectMapper.writeValueAsString(userDTO);
    }

    @Test
    @DisplayName("회원가입 - 실패")
    void register_fail() throws Exception {
        String url = "/signup";
        String email = "test@yu.ac.kr";
        String username = "testUser";
        String password = "1234";
        User beforeRegister = createUser(email);
        String content = createUserDto(email, username, password);

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"이메일 인증이 완료되지 않은 사용자입니다.\"}"));
                });
        User user = userRepository.findByEmail(email).orElseThrow();
        assertFalse(user.getUsername().equals("testUser"));
        assertFalse(passwordEncoder.matches("1234", user.getPassword()));
    }

    @Test
    @DisplayName("로그인 - 성공")
    void login_success() throws Exception {
        String url = "/signin";
        String email = "test@yu.ac.kr";
        String username = "testUser";
        String password = "1234";
        register(email, username, password);
        String content = createUserDto(email, username, "1234");

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertNotNull(response.getCookie("X-AUTH-REFRESH-TOKEN"));
                    assertTrue(response.getContentAsString().contains("accessToken"));
                });
    }

    private void register(String email, String username, String password) {
        User user = createUser(email);
        user.updateUserInfo(username, passwordEncoder.encode(password));
        user.updateEmailConfirm(true);
    }

    @Test
    @DisplayName("로그인 - 실패")
    void login_fail() throws Exception {
        String url = "/signin";
        String email = "test@yu.ac.kr";
        String username = "testUser";
        String password = "1234";
        register(email, username, password);
        String content = createUserDto(email, username, "12345");

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"비밀번호가 일치하지 않습니다.\"}"));
                });
    }

}