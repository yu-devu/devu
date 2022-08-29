package com.devu.backend.controller.user;

import com.devu.backend.config.auth.token.RefreshToken;
import com.devu.backend.config.auth.token.TokenService;
import com.devu.backend.controller.validation.EmailCheck;
import com.devu.backend.controller.validation.UserKeyCheck;
import com.devu.backend.entity.User;
import com.devu.backend.repository.RefreshTokenRepository;
import com.devu.backend.repository.UserRepository;
import com.devu.backend.service.CookieService;
import com.devu.backend.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {

    public static final String SUCCESS_EMAIL = "test@yu.ac.kr";
    public static final String FAIL_EMAIL = "test@naver.com";
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private TokenService tokenService;
    @Autowired private CookieService cookieService;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("이메일 입력 검증 - 실패")
    void emailValidationFail() throws Exception {
        String url = "/email";
        UserEmailRequestDto dto = UserEmailRequestDto.builder()
                .email(FAIL_EMAIL).build();
        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    String contentAsString = response.getContentAsString();
                    assertThat(contentAsString).isEqualTo(EmailCheck.ERROR_MESSAGE);
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("이메일 입력 검증 - 성공")
    void emailValidationSuccess() throws Exception {
        JSONParser parser = new JSONParser();
        String url = "/email";
        UserEmailRequestDto dto = UserEmailRequestDto.builder()
                .email(SUCCESS_EMAIL).build();
        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    String contentAsString = response.getContentAsString();
                    JSONObject jsonObject = (JSONObject) parser.parse(contentAsString);
                    assertThat(jsonObject.get("email")).isEqualTo(SUCCESS_EMAIL);
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("이메일 전송 - 성공")
    void sendEmail_success() throws Exception {
        String url = "/email";
        UserEmailRequestDto dto = UserEmailRequestDto.builder()
                .email(SUCCESS_EMAIL).build();
        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        actions
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        assertNotNull(user);
        assertNotNull(user.getEmailAuthKey());
        assertFalse(user.isEmailConfirm());
    }

    @Test
    @DisplayName("이메일 전송 - 실패")
    void sendEmail_fail() throws Exception {
        String url = "/email";
        UserEmailRequestDto dto = UserEmailRequestDto
                .builder()
                .email(SUCCESS_EMAIL).build();
        String content = objectMapper.writeValueAsString(dto);

        User user = userService.createUserBeforeEmailValidation(dto.getEmail());
        user.updateEmailConfirm(true);
        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        actions
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertEquals("{\"error\":\"이미 가입 완료된 이메일입니다.\"}", response.getContentAsString());
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("이메일 재 전송")
    void resendEmail() throws Exception {
        String url = "/email";
        UserEmailRequestDto dto = UserEmailRequestDto.builder()
                .email(SUCCESS_EMAIL).build();
        String content = objectMapper.writeValueAsString(dto);

        userService.createUserBeforeEmailValidation(dto.getEmail());
        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        actions
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertEquals("{\"error\":\"이메일 재전송 완료\"}", response.getContentAsString()); })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));


        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();

        assertNotEquals("test-key", user.getEmailAuthKey());
    }

    @Test
    @DisplayName("이메일 검증키 검증 - 실패")
    void userKeyValidationFail() throws Exception {
        String url = "/key";
        UserKeyRequestDto dto = UserKeyRequestDto.builder()
                .userKey("").build();
        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    String contentAsString = response.getContentAsString();
                    assertThat(contentAsString).isEqualTo(UserKeyCheck.ERROR_MESSAGE);
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("이메일 인증 - 성공")
    void checkEmail_success() throws Exception {
        String url = "/key";
        User savedUser = createUser(SUCCESS_EMAIL);

        UserKeyRequestDto dto = UserKeyRequestDto.builder()
                .userKey(savedUser.getEmailAuthKey()).build();
        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        actions
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        assertTrue(savedUser.isEmailConfirm());
    }

    @Test
    @DisplayName("이메일 인증 - 실패")
    void checkEmail_fail() throws Exception {
        String url = "/key";
        String wrongKey = "abcdefgh";
        User savedUser = createUser(SUCCESS_EMAIL);

        UserKeyRequestDto dto = UserKeyRequestDto.builder()
                .userKey(wrongKey).build();
        String content = objectMapper.writeValueAsString(dto);

        ResultActions actions = mockMvc.perform(post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        actions
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertEquals("{\"error\":\"입력한 인증키가 일치하지 않습니다.\"}", response.getContentAsString());
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        assertFalse(savedUser.isEmailConfirm());
    }

    private User createUser(String email) throws Exception {
        User user = User.builder()
                .email(email)
                .emailConfirm(false)
                .build();
        return userService.createUserBeforeEmailValidation(user.getEmail());
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

        actions
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

        User user = userRepository.findByEmail(email).orElseThrow();
        assertEquals("testUser", user.getUsername());
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
                    assertEquals("{\"error\":\"이메일 인증이 완료되지 않은 사용자입니다.\"}", response.getContentAsString());
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        User user = userRepository.findByEmail(email).orElseThrow();
        assertNull(user.getUsername());
        assertNull(user.getPassword());
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
                    assertNotNull(response.getHeader("X-AUTH-ACCESS-TOKEN"));
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    private void register(String email, String username, String password) throws Exception {
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
                    assertEquals("{\"error\":\"비밀번호가 일치하지 않습니다.\"}", response.getContentAsString());
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
        String url = "/logout";
        String accessToken = tokenService.createAccessToken("test@yu.ac.kr");
        String refreshToken = tokenService.createRefreshToken("test@yu.ac.kr");
        RefreshToken token = RefreshToken.builder().refreshToken(refreshToken).build();
        refreshTokenRepository.save(token);
        ResponseCookie cookie = cookieService.createCookie("X-AUTH-REFRESH-TOKEN", refreshToken);

        assertNotNull(refreshTokenRepository.findByRefreshToken(refreshToken));
        mockMvc.perform(
                post(url)
                    .cookie(new Cookie(cookie.getName(), cookie.getValue()))
                    .header("X-AUTH-ACCESS-TOKEN", accessToken))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertEquals(0, response.getCookie("X-AUTH-REFRESH-TOKEN").getMaxAge());
                })
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

        assertNull(refreshTokenRepository.findByRefreshToken(refreshToken));
    }
}