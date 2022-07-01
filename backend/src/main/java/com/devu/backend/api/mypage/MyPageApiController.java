package com.devu.backend.api.mypage;

import com.devu.backend.config.auth.UserDetailsImpl;
import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.controller.user.UserDTO;
import com.devu.backend.controller.user.UserRequestUpdateDto;
import com.devu.backend.entity.User;
import com.devu.backend.service.PostService;
import com.devu.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MyPageApiController {
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/myPosts")
    public ResponseEntity<?> myPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            User user = userDetails.getUser();
            List<PostResponseDto> chats = postService.findAllChatsByUser(user);
            List<PostResponseDto> studies = postService.findAllStudiesByUser(user);
            List<PostResponseDto> questions = postService.findAllQuestionsByUser(user);
            List<PostResponseDto> collect
                    = Stream.concat(chats.stream(), studies.stream()).collect(Collectors.toList());
            List<PostResponseDto> posts
                    = Stream.concat(collect.stream(), questions.stream()).collect(Collectors.toList());
            return ResponseEntity.ok(posts);
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/myLikes")
    public ResponseEntity<?> myLikes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            User user = userDetails.getUser();
            List<PostResponseDto> chats = postService.findAllLikeChatsByUser(user);
            List<PostResponseDto> studies = postService.findAllLikeStudiesByUser(user);
            List<PostResponseDto> questions = postService.findAllLikeQuestionsByUser(user);
            List<PostResponseDto> collect
                    = Stream.concat(chats.stream(), studies.stream()).collect(Collectors.toList());
            List<PostResponseDto> likes
                    = Stream.concat(collect.stream(), questions.stream()).collect(Collectors.toList());
            return ResponseEntity.ok(likes);
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = userDetails.getUser().getUsername();
            userService.deleteUser(username, request, response);
            return ResponseEntity.ok().body(username+ " 님이 탈퇴했습니다.");
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @PatchMapping("/username")
    public ResponseEntity<?> updateUsername(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody UserRequestUpdateDto userUpdateDto) {
        try {
            String username = userDetails.getUser().getUsername();
            UserDTO dto = userService.updateUsername(username, userUpdateDto.getUsername());
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }
}
