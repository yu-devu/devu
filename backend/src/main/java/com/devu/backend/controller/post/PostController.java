package com.devu.backend.controller.post;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.post.*;
import com.devu.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //자유 게시판 리스트 get
    @GetMapping("/chats")
    public ResponseEntity<?> getChats(@PageableDefault(size = 20)Pageable pageable) {
        try {
            List<ResponsePostDto> chats = postService.findAllChats(pageable).getContent();
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //스터디 게시판 리스트 get
    @GetMapping("/studies")
    public ResponseEntity<?> getStudies(@PageableDefault(size = 20)Pageable pageable) {
        try {
            List<ResponsePostDto> studies = postService.findAllStudies(pageable).getContent();
            return ResponseEntity.ok(studies);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //질문 게시판 리스트 get
    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions(@PageableDefault(size = 20)Pageable pageable) {
        try {
            List<ResponsePostDto> questions = postService.findAllQuestions(pageable).getContent();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //자유 게시판 글 작성
    @PostMapping("/chat")
    public ResponseEntity<?> createChat(@RequestBody RequestPostCreateDto requestPostDto) {
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
    public ResponseEntity<?> createStudy(@RequestBody RequestPostCreateDto requestPostDto) {
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
    public ResponseEntity<?> createQuestion(@RequestBody RequestPostCreateDto requestPostDto) {
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
