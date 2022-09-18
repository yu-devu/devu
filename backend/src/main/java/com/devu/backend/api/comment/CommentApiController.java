package com.devu.backend.api.comment;

import com.devu.backend.common.exception.CommentNotFoundException;
import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.repository.comment.CommentRepository;
import com.devu.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Comment comment = commentService.saveComment(requestDto);
            CommentResponseDto responseDto = getCommentResponseDto(comment);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    private CommentResponseDto getCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .username(comment.getUser().getUsername())
                .contents(comment.getContents())
                .title(comment.getPost().getTitle())
                .group(comment.getGroupNum())
                .createAt(comment.getCreateAt())
                .lastModifiedAt(comment.getLastModifiedAt())
                .build();
    }

    @PostMapping("/reComments")
    ResponseEntity<?> createReComment(@RequestBody CommentCreateRequestDto requestDto) {
        try {
            Comment comment = commentService.saveReComment(requestDto);
            CommentResponseDto responseDto = getReCommentResponseDto(comment);
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    private CommentResponseDto getReCommentResponseDto(Comment comment) {
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .commentId(comment.getId())
                .username(comment.getUser().getUsername())
                .contents(comment.getContents())
                .title(comment.getPost().getTitle())
                .parent(comment.getParent())
                .group(comment.getGroupNum())
                .createAt(comment.getCreateAt())
                .lastModifiedAt(comment.getLastModifiedAt())
                .build();
        return responseDto;
    }


    @PatchMapping("/comments/{id}")
    ResponseEntity<?> updateComment(@PathVariable(name = "id") Long commentId,
                                    @RequestBody CommentUpdateRequestDto updateRequestDto) {
        try {
            updateRequestDto.setCommentId(commentId);
            Comment comment = commentService.updateComment(updateRequestDto);
            CommentResponseDto responseDto = getCommentResponseDto(comment);
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
