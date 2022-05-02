package com.devu.backend.api.status;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StatusApiController {
    private final PostService postService;

    @PostMapping("/change/study_status")
    public ResponseEntity<?> changeStudyStatus(@RequestBody RequestStatusDto requestStatusDto) {
        try{
            ResponseStatusDto responseStatusDto
                    = postService.updateStudyStatus(requestStatusDto.getPostId(), requestStatusDto.getUsername());
            return ResponseEntity.ok().body(responseStatusDto);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }
    @PostMapping("/change/question_status")
    public ResponseEntity<?> changeQuestionStatus(@RequestBody RequestStatusDto requestStatusDto) {
        try{
            ResponseStatusDto responseStatusDto
                    = postService.updateQuestionStatus(requestStatusDto.getPostId(), requestStatusDto.getUsername());
            return ResponseEntity.ok().body(responseStatusDto);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }
}
