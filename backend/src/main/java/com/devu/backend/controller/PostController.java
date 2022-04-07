package com.devu.backend.controller;

import com.devu.backend.entity.post.Post;
import com.devu.backend.entity.post.Study;
import com.devu.backend.entity.post.StudyStatus;
import com.devu.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/community")
public class PostController {

    private final PostService postService;

    @GetMapping
    private ResponseEntity home() {
        return ResponseEntity.ok().body("커뮤니티 홈 테스트");
    }

    //Posts 정보 get
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //자유 게시판 글 작성
    @PostMapping("/post")
    public ResponseEntity<?> createPost() {
        Post build = Post.builder()
                .content("test")
                .title("test")
                .build();
        postService.createPost(build);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/studies")
    public ResponseEntity<?> getStudies() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //스터디 게시판 글 작성
    @PostMapping("/study")
    public ResponseEntity<?> createStudy() {
        Study build = Study.builder()
                .content("test")
                .title("test")
                .studyStatus(StudyStatus.ACTIVE)
                .build();
        postService.createPost(build);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
