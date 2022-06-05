package com.devu.backend.api.comment;

import com.devu.backend.common.exception.CommentNotFoundException;
import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.repository.comment.CommentRepository;
import com.devu.backend.service.CommentService;
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
@RequestMapping("/api")
public class CommentApiController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @PostMapping("/comments")
    ResponseEntity<?> createComment(@RequestBody CommentCreateRequestDto requestDto) {
        try {
            log.info("Post Id : {}",requestDto.getPostId());
            log.info("Username : {}",requestDto.getUsername());
            Comment comment = commentService.saveComment(requestDto);

            log.info("Comment ID : {} is created by username : {} where postTitle : {}, group {}",
                    comment.getId(),
                    comment.getUser().getUsername(),
                    comment.getPost().getTitle(),
                    comment.getGroupNum());
            CommentResponseDto responseDto = CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .username(comment.getUser().getUsername())
                    .contents(comment.getContents())
                    .title(comment.getPost().getTitle())
                    .group(comment.getGroupNum())
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

    @PostMapping("/reComments")
    ResponseEntity<?> createReComment(@RequestBody CommentCreateRequestDto requestDto) {
        try {
            log.info("Post Id : {}",requestDto.getPostId());
            log.info("Username : {}",requestDto.getUsername());
            Comment comment = commentService.saveReComment(requestDto);

            log.info("Comment ID : {} is created by username : {} where postTitle : {}, group {}, from who {}",
                    comment.getId(),
                    comment.getUser().getUsername(),
                    comment.getPost().getTitle(),
                    comment.getGroupNum(),
                    comment.getParent());
            CommentResponseDto responseDto = CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .username(comment.getUser().getUsername())
                    .title(comment.getPost().getTitle())
                    .group(comment.getGroupNum())
                    .contents(comment.getContents())
                    .parent(comment.getParent())
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


    @PatchMapping("/comments/{id}")
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

    @DeleteMapping("/comments/{id}")
    ResponseEntity<?> deleteComment(@PathVariable(name = "id") Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
            commentService.deleteComment(comment);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @DeleteMapping("/reComments/{id}")
    ResponseEntity<?> deleteReComment(@PathVariable(name = "id") Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
            Comment originalComment = commentRepository.findById(comment.getGroupNum()).orElseThrow(CommentNotFoundException::new);
            commentService.deleteReComment(originalComment, comment);
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
