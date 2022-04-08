package com.devu.backend.controller.post;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.post.*;
import com.devu.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/community")
public class PostController {

    private final PostService postService;

    @GetMapping
    private ResponseEntity home() {
        return ResponseEntity.ok().body("커뮤니티 홈 테스트");
    }

    //Posts 정보 get
    @GetMapping("/chats")
    public ResponseEntity<?> getPosts() {
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @GetMapping("/studies")
    public ResponseEntity<?> getStudies() {
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //자유 게시판 글 작성
    @PostMapping("/chat")
    public ResponseEntity<?> createChat(@RequestBody RequestCreateDto requestPostDto) {
        try {
            Chat chat = postService.createChat(requestPostDto);
            log.info("{}님이 chat 게시글 생성",chat.getUser().getUsername());
            ResponsePostDto postDto = ResponsePostDto.builder()
                    .title(chat.getTitle())
                    .username(chat.getUser().getUsername())
                    .build();
            return ResponseEntity.ok(postDto);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //스터디 게시판 글 작성
    @PostMapping("/study")
    public ResponseEntity<?> createStudy(@RequestBody RequestCreateDto requestPostDto) {
        try {
            Study study = postService.createStudy(requestPostDto);
            log.info("{}님이 Study 게시글 생성",study.getUser().getUsername());
            ResponsePostDto postDto = ResponsePostDto.builder()
                    .title(study.getTitle())
                    .username(study.getUser().getUsername())
                    .build();
            return ResponseEntity.ok(postDto);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //질문 게시판 글 작성
    @PostMapping("/question")
    public ResponseEntity<?> createQuestion(@RequestBody RequestCreateDto requestPostDto) {
        try {
            Question question = postService.createQuestion(requestPostDto);
            log.info("{}님이 Question 게시글 생성",question.getUser().getUsername());
            ResponsePostDto postDto = ResponsePostDto.builder()
                    .title(question.getTitle())
                    .username(question.getUser().getUsername())
                    .build();
            return ResponseEntity.ok(postDto);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }
}
