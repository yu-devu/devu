package com.devu.backend.api.like;

import com.devu.backend.common.exception.UnlikedPostException;
import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LikeApiController {
    private final LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<?> addLike(@RequestBody RequestLikeDto requestLikeDto) {
        try {
            if (!likeService.isAlreadyLiked(requestLikeDto.getUsername(), requestLikeDto.getPostId())) {
                likeService.addLike(requestLikeDto.getUsername(), requestLikeDto.getPostId());
                log.info("Post {} is liked by {}",requestLikeDto.getPostId(),requestLikeDto.getUsername());
                return ResponseEntity.ok().body("좋아요");
            }
            likeService.dislike(requestLikeDto.getUsername(), requestLikeDto.getPostId());
            log.info("Post {} is disliked by {}",requestLikeDto.getPostId(),requestLikeDto.getUsername());
            return ResponseEntity.ok().body("좋아요 해제");
        } catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }
}
