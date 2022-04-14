package com.devu.backend.service;

import com.devu.backend.common.exception.AlreadyLikedException;
import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.entity.Like;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Post;
import com.devu.backend.repository.LikeRepository;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void addLike(String username, Long postId) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        //이미 좋아요가 되어있으면 throw
        if (isAlreadyLiked(user,post)){
            throw new AlreadyLikedException();
        }
        likeRepository.save(Like.builder()
                .post(post)
                .user(user)
                .build()
        );
    }

    /*
    * isPresent() true => 이미 좋아요를 누름
    * */
    private boolean isAlreadyLiked(User user, Post post) {
        return likeRepository.findByUserAndPost(user, post).isPresent();
    }
}
