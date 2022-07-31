package com.devu.backend.controller.admin;

import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.entity.User;
import com.devu.backend.service.EmailService;
import com.devu.backend.service.PostService;
import com.devu.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

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
        for (User user : users) {
            List<PostResponseDto> allChatsByUser = postService.findAllChatsByUser(user);
            List<PostResponseDto> allStudiesByUser = postService.findAllStudiesByUser(user);
            List<PostResponseDto> allQuestionsByUser = postService.findAllQuestionsByUser(user);
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
        model.addAttribute("users", userDTOS);
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
        model.addAttribute("username", user.getUsername());
        List<PostResponseDto> studies = postService.findAllStudiesByUser(user);
        List<PostResponseDto> likeStudies = postService.findAllLikeStudiesByUser(user);
        model.addAttribute("studies", studies);
        model.addAttribute("likStudies", likeStudies);
        return "user-info";
    }
}
