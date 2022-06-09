package com.devu.backend.api.hit;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.post.Post;
import com.devu.backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class HitApiController {
    private final LikeService likeService;

    @GetMapping("/hits")
    public ResponseEntity<?> getHits(@RequestParam(name = "postId") Long postId) {
        try {
            Post post = likeService.findPostById(postId);
            ResponseHitsDto responseDto = ResponseHitsDto.builder()
                    .hits(post.getHit())
                    .postId(post.getId()).build();
            log.info("Post Id {} has {} hits", responseDto.getPostId(), responseDto.getHits());
            return ResponseEntity.ok().body(responseDto);
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }


}
