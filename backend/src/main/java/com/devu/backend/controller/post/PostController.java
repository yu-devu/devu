package com.devu.backend.controller.post;

import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.post.*;
import com.devu.backend.repository.PostRepository;
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
    private final PostRepository postRepository;

    @GetMapping
    private ResponseEntity home() {
        return ResponseEntity.ok().body("커뮤니티 홈 테스트");
    }

    //자유 게시판 리스트 get
    @GetMapping("/chats")
    public ResponseEntity<?> getChats(@PageableDefault(size = 20)Pageable pageable) {
        try {
            List<PostResponseDto> chats = postService.findAllChats(pageable).getContent();
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
            List<PostResponseDto> studies = postService.findAllStudies(pageable).getContent();
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
            List<PostResponseDto> questions = postService.findAllQuestions(pageable).getContent();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //자유 게시판 게시글 조회 By Post ID
    @GetMapping("/chats/{id}")
    public ResponseEntity<?> getChatById(@PathVariable("id") Long id) {
        try {
            PostResponseDto chat = postService.findChatById(id);
            return ResponseEntity.ok().body(chat);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //스터디 게시판 게시글 조회 By Post ID
    @GetMapping("/studies/{id}")
    public ResponseEntity<?> getStudyById(@PathVariable("id") Long id) {
        try {
            PostResponseDto chat = postService.findStudyById(id);
            return ResponseEntity.ok().body(chat);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //질문 게시판 게시글 조회 By Post ID
    @GetMapping("/questions/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable("id") Long id) {
        try {
            PostResponseDto chat = postService.findQuestionById(id);
            return ResponseEntity.ok().body(chat);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    /*
    * Controller에서 객체 생성 하지 않는 방향
    * */
    //자유 게시판 글 작성
    @PostMapping("/chat")
    public ResponseEntity<?> createChat(PostRequestCreateDto requestPostDto) {
        try {
            PostResponseDto responseDto = postService.createChat(requestPostDto);
            return ResponseEntity.ok(responseDto);
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
    public ResponseEntity<?> createStudy(PostRequestCreateDto requestPostDto) {
        try {
            PostResponseDto responseDto = postService.createStudy(requestPostDto);
            return ResponseEntity.ok(responseDto);
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
    public ResponseEntity<?> createQuestion(PostRequestCreateDto requestPostDto) {
        try {
            PostResponseDto responseDto = postService.createQuestion(requestPostDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //chat update
    @PatchMapping("/chat/{id}")
    public ResponseEntity<?> updateChat(@PathVariable("id") Long id, PostRequestUpdateDto updateDto) {
        try {
            Chat chat = (Chat) postRepository.findById(id).orElseThrow(PostNotFoundException::new);
            postService.updateChat(chat, updateDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //study update
    @PatchMapping("/study/{id}")
    public ResponseEntity<?> updateStudy(@PathVariable("id") Long id, PostRequestUpdateDto updateDto) {
        try {
            Study study = (Study) postRepository.findById(id).orElseThrow(PostNotFoundException::new);
            postService.updateStudy(study, updateDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //qna update
    @PatchMapping("/question/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable("id") Long id, PostRequestUpdateDto updateDto) {
        try {
            Question question = (Question) postRepository.findById(id).orElseThrow(PostNotFoundException::new);
            postService.updateQuestion(question, updateDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @DeleteMapping("/chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable("id") Long id) {
        try {
            Chat chat = (Chat) postRepository.findById(id).orElseThrow(PostNotFoundException::new);
            postService.deleteChat(chat);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @DeleteMapping("/study/{id}")
    public ResponseEntity<?> deleteStudy(@PathVariable("id") Long id) {
        try {
            Study study = (Study) postRepository.findById(id).orElseThrow(PostNotFoundException::new);
            postService.deleteStudy(study);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("id") Long id) {
        try {
            Question question = (Question) postRepository.findById(id).orElseThrow(PostNotFoundException::new);
            postService.deleteQuestion(question);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }
}
