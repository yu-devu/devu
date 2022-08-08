package com.devu.backend.controller.admin;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.controller.post.PostRequestCreateDto;
import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Post;
import com.devu.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;
    private final EmailService emailService;

    @GetMapping
    private String adminHome(Model model) {
        List<User> users = userService.getUsers();
        List<UserDTO> userDTOS = new ArrayList<>();
        int sumOfPosts = 0;
        int sumOfChats = 0;
        int sumOfStudies = 0;
        int sumOfQuestions = 0;

        for (User user : users) {
            List<PostResponseDto> allChatsByUser = postService.findAllChatsByUser(user);
            List<PostResponseDto> allStudiesByUser = postService.findAllStudiesByUser(user);
            List<PostResponseDto> allQuestionsByUser = postService.findAllQuestionsByUser(user);
            sumOfPosts += allChatsByUser.size();
            sumOfPosts += allQuestionsByUser.size();
            sumOfPosts += allStudiesByUser.size();
            sumOfChats += allChatsByUser.size();
            sumOfStudies += allStudiesByUser.size();
            sumOfQuestions += allQuestionsByUser.size();
            UserDTO userDto = UserDTO.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .emailValidation(user.isEmailConfirm())
                    .chatSize(allChatsByUser.size())
                    .questionsSize(allQuestionsByUser.size())
                    .studiesSize(allStudiesByUser.size())
                    .build();
            userDTOS.add(userDto);
        }
        SumDTO sumDTO = SumDTO.builder()
                .sumOfUsers((long) users.size())
                .sumOfPosts((long) sumOfPosts)
                .sumOfStudies((long) sumOfStudies)
                .sumOfChats((long) sumOfChats)
                .sumOfQuestions((long) sumOfQuestions)
                .build();
        model.addAttribute("users", userDTOS);
        model.addAttribute("sums", sumDTO);
        return "main";
    }

    @PostMapping("/user")
    private String createMockUser(Model model) {
        String username = emailService.createKey();
        String email = username + "@test.com";
        String password = "test";
        UserDTO userDTO = UserDTO.builder()
                .email(email)
                .username(username)
                .emailValidation(true)
                .password(password)
                .build();
        User userByAdmin = userService.createUserByAdmin(userDTO);
        log.info("Admin creates User {}",userByAdmin.getUsername());
        return "redirect:/admin";
    }


    @PostMapping("/{userId}")
    private String deleteUser(@PathVariable Long userId, Model model) {
        log.info("userId = {}", userId);
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

    @GetMapping("/userInfo")
    private String getUserInfo(@RequestParam Long userId, Model model) {
        log.info("Query parameter = {}", userId);
        User user = userService.getUserById(userId);
        List<Post> allPosts = postService.findAllPosts(user);
        int receiveLikes = 0;
        int receiveComments = 0;
        calculateReceiveValue(allPosts, receiveLikes, receiveComments);
        UserDTO userDTO = UserDTO.builder()
                .username(user.getUsername())
                .userId(user.getId())
                .email(user.getEmail())
                .receivedComments(receiveComments)
                .receivedLikes(receiveLikes)
                .build();
        model.addAttribute("user", userDTO);
        addStudyAttribute(model, user);
        return "user-info";
    }

    private void calculateReceiveValue(List<Post> allPosts, int receiveLikes, int receiveComments) {
        for (Post post : allPosts) {
            receiveLikes += post.getLikes().size();
            receiveComments += post.getComments().size();
        }
    }

    private void addStudyAttribute(Model model, User user) {
        List<PostResponseDto> studies = postService.findAllStudiesByUser(user);
        List<PostResponseDto> likeStudies = postService.findAllLikeStudiesByUser(user);
        model.addAttribute("studies", studies);
        model.addAttribute("likStudies", likeStudies);
    }

    @PostMapping("/post/{userId}")
    private String createMockPost(@RequestParam String type,
                                  @PathVariable Long userId ,
                                  RedirectAttributes redirectAttributes) {
        try {
            redirectAttributes.addAttribute("userId", userId);
            User user = userService.getUserById(userId);
            List<String> randomTags = createRandomTags();
            PostRequestCreateDto createDto = PostRequestCreateDto.builder()
                    .username(user.getUsername())
                    .title(user.getUsername())
                    .content("test")
                    .tags(randomTags)
                    .build();
            switch (type) {
                case "study":
                    createDto.setTitle(createDto.getTitle() + "의 스터디 게시글");
                    PostResponseDto study = postService.createStudy(createDto);
                    log.info("Mock study is created, id = {}",study.getId());
                    break;
                case "question":
                    createDto.setTitle(createDto.getTitle() + "의 질문 게시글");
                    PostResponseDto question = postService.createQuestion(createDto);
                    log.info("Mock question is created, id = {}",question.getId());
                    break;
                case "chat":
                    createDto.setTitle(createDto.getTitle() + "의 채팅 게시글");
                    PostResponseDto chat = postService.createChat(createDto);
                    log.info("Mock chat is created, id = {}",chat.getId());
                    break;
            }
            return "redirect:/admin";
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return "error";
        }
    }

    private List<String> createRandomTags() {
        List<String> tagData = getTagData();
        Random rnd = new Random();
        int index1 = rnd.nextInt(7); // 0~6 까지 랜덤
        int index2 = rnd.nextInt(7); // 0~6 까지 랜덤
        while (index1 == index2) {
            index2 = rnd.nextInt(7);
        }
        List<String> tags = new ArrayList<>();
        tags.add(tagData.get(index1));
        tags.add(tagData.get(index2));
        return tags;
    }

    private List<String> getTagData() {
        List<String> tagData = new ArrayList<>();
        tagData.add("Spring");
        tagData.add("C");
        tagData.add("React");
        tagData.add("Docker");
        tagData.add("Python");
        tagData.add("Java");
        tagData.add("NodeJS");
        return tagData;
    }
}
