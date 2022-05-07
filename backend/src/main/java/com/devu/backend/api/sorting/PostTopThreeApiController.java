package com.devu.backend.api.sorting;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.controller.post.PostResponseDto;
import com.devu.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostTopThreeApiController {
    private final PostService postService;

    @GetMapping("/top3_chats_by_hit")
    ResponseEntity<?> getTop3ChatByHits() {
        try {
            List<PostResponseDto> responseDtos = postService.getTop3ChatByHits();
            return ResponseEntity.ok().body(responseDtos);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/top3_chats_by_likes")
    ResponseEntity<?> getTop3ChatByLikes() {
        try {
            List<PostResponseDto> responseDtos = postService.getTop3ChatByLikes();
            return ResponseEntity.ok().body(responseDtos);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/top3_studies_by_hit")
    ResponseEntity<?> getTop3StudyByHits() {
        try {
            List<PostResponseDto> responseDtos = postService.getTop3StudyByHits();
            return ResponseEntity.ok().body(responseDtos);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/top3_studies_by_likes")
    ResponseEntity<?> getTop3StudyByLikes() {
        try {
            List<PostResponseDto> responseDtos = postService.getTop3StudyByLikes();
            return ResponseEntity.ok().body(responseDtos);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/top3_questions_by_hit")
    ResponseEntity<?> getTop3QuestionByHits() {
        try {
            List<PostResponseDto> responseDtos = postService.getTop3QuestionByHits();
            return ResponseEntity.ok().body(responseDtos);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/top3_questions_by_likes")
    ResponseEntity<?> getTop3QuestionByLikes() {
        try {
            List<PostResponseDto> responseDtos = postService.getTop3QuestionByLikes();
            return ResponseEntity.ok().body(responseDtos);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

}
