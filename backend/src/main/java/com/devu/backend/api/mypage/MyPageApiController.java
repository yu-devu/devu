package com.devu.backend.api.mypage;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Post;
import com.devu.backend.service.PostService;
import com.devu.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /*
    * 추후 AuthenticationPrincipal로 변경 필요
    * */
    @GetMapping("/myPosts/{username}")
    public ResponseEntity<?> getAllMyPosts(@PathVariable("username") String username) {
        try{
            User user = userService.getUserByUsername(username);
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

    @GetMapping("/myLikes/{username}")
    public ResponseEntity<?> getAllMyLikes(@PathVariable("username") String username) {
        try {
            User user = userService.getUserByUsername(username);
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
}
