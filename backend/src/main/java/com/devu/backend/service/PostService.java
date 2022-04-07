package com.devu.backend.service;

import com.devu.backend.entity.post.Post;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(Post post) {
       return postRepository.save(post);
    }
}
