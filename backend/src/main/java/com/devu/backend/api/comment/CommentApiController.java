package com.devu.backend.api.comment;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/comment")
    ResponseEntity<?> createComment(@RequestBody CommentCreateRequestDto requestDto) {
        try {
            log.info("Post Id : {}",requestDto.getPostId());
            log.info("User Id : {}",requestDto.getUserId());
            Comment comment = commentService.saveComment(requestDto);
            log.info("Comment ID : {} is created by username : {} where postTitle : {}",
                    comment.getId(),
                    comment.getUser().getUsername(),
                    comment.getPost().getTitle());
            CommentResponseDto responseDto = CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .username(comment.getUser().getUsername())
                    .title(comment.getPost().getTitle())
                    .build();
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @PatchMapping("/comment/{id}")
    ResponseEntity<?> updateComment(@PathVariable(name = "id") Long commentId,
                                    @RequestBody CommentUpdateRequestDto updateRequestDto) {
        try {
            updateRequestDto.setCommentId(commentId);
            Comment comment = commentService.updateComment(updateRequestDto);
            log.info("Comment ID : {} is updated by username : {} where postTitle : {}",
                    comment.getId(),
                    comment.getUser().getUsername(),
                    comment.getPost().getTitle());
            CommentResponseDto responseDto = CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .username(comment.getUser().getUsername())
                    .title(comment.getPost().getTitle())
                    .build();
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }
}
