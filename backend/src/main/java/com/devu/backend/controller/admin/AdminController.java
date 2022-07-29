package com.devu.backend.controller.admin;

import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.entity.User;
import com.devu.backend.service.PostService;
import com.devu.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;

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
}