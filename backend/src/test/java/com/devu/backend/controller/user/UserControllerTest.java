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
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, emailService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("????????? ?????? - ??????")
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
    @DisplayName("????????? ?????? - ??????")
    void sendEmail_fail() throws Exception {
        String url = "/email";
        String email = "test@yu.ac.kr";

        User user = userService.createUser(email);
        user.updateEmailConfirm(true);
        ResultActions actions = mockMvc.perform(post(url)
                .param("email", email));

        actions
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertTrue(response.getContentAsString().equals("{\"error\":\"?????? ?????? ????????? ??????????????????.\"}"));
                });
    }

    @Test
    @DisplayName("????????? ??? ??????")
    void resendEmail() throws Exception {
        String url = "/email";
        String email = "test@yu.ac.kr";

        userService.createUser(email);
        ResultActions actions = mockMvc.perform(post(url)
                .param("email", email));

        actions
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    System.out.println(response.getContentAsString());
                    assertTrue(response.getContentAsString().equals("{\"error\":\"????????? ????????? ??????\"}"));
                });
        User user = userRepository.findByEmail(email).orElseThrow();

        assertFalse("test-key".equals(user.getEmailAuthKey()));
    }

    @Test
    @DisplayName("????????? ?????? - ??????")
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
    @DisplayName("????????? ?????? - ??????")
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
                    assertTrue(response.getContentAsString().equals("{\"error\":\"????????? ???????????? ???????????? ????????????.\"}"));
                });
        assertFalse(savedUser.isEmailConfirm());
    }

    private User createUser(String email) throws Exception {
        User user = User.builder()
                .email(email)
                .emailConfirm(false)
                .build();
        User savedUser = userService.createUser(user.getEmail());
        return savedUser;
    }

    @Test
    @DisplayName("???????????? - ??????")
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
    @DisplayName("???????????? - ??????")
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
                    assertTrue(response.getContentAsString().equals("{\"error\":\"????????? ????????? ???????????? ?????? ??????????????????.\"}"));
                });
        User user = userRepository.findByEmail(email).orElseThrow();
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    @DisplayName("????????? - ??????")
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
                    assertNotNull(response.getHeader("X-AUTH-ACCESS-TOKEN"));
                });
    }

    private void register(String email, String username, String password) throws Exception {
        User user = createUser(email);
        user.updateUserInfo(username, passwordEncoder.encode(password));
        user.updateEmailConfirm(true);
    }

    @Test
    @DisplayName("????????? - ??????")
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
                    assertTrue(response.getContentAsString().equals("{\"error\":\"??????????????? ???????????? ????????????.\"}"));
                });
    }

}