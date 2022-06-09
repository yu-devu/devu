package com.devu.backend.api.like;

import com.devu.backend.common.exception.UnlikedPostException;
import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Post;
import com.devu.backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LikeApiController {
    private final LikeService likeService;

    @GetMapping("/like")
    public ResponseEntity<?> getLike(@RequestParam(name = "postId") Long postId) {
        try {
            Post post = likeService.findPostById(postId);
            ResponseLikeSizeDto responseDto = ResponseLikeSizeDto.builder()
                    .likeSize(post.getLikes().size())
                    .postId(postId).build();
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @PostMapping("/like")
    public ResponseEntity<?> addLike(@RequestBody RequestLikeDto requestLikeDto) {
        try {
            User user = likeService.findUserByUsername(requestLikeDto.getUsername());
            Post post = likeService.findPostById(requestLikeDto.getPostId());

            if (!likeService.isAlreadyLiked(user,post)) {
                likeService.addLike(user,post);
                log.info("Post {} is liked by {}",requestLikeDto.getPostId(),requestLikeDto.getUsername());
                ResponseLikeDto likeDto = ResponseLikeDto.builder()
                        .liked(true)
                        .username(requestLikeDto.getUsername())
                        .build();
                return ResponseEntity.ok().body(likeDto);
            }
            likeService.dislike(user,post);
            log.info("Post {} is disliked by {}",requestLikeDto.getPostId(),requestLikeDto.getUsername());
            ResponseLikeDto likeDto = ResponseLikeDto.builder()
                    .liked(false)
                    .username(requestLikeDto.getUsername())
                    .build();
            return ResponseEntity.ok().body(likeDto);
        } catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }
}
