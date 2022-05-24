package com.devu.backend.service;

import com.devu.backend.common.exception.LikeNotFoundException;
import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.entity.Like;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Post;
import com.devu.backend.repository.LikeRepository;
import com.devu.backend.repository.post.PostRepository;
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

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    /*
    * like => 4 query
    * */
    @Transactional(readOnly = false)
    public void addLike(User user,Post post) {
        Like like = likeRepository.save(Like.builder()
                .post(post)
                .user(user)
                .build()
        );
        like.changePost(post);
    }

    /*
    * isPresent() true => 이미 좋아요를 누름
    * */
    public boolean isAlreadyLiked(User user,Post post) {
        return likeRepository.findByUserAndPost(user, post).isPresent();
    }

    /*
    * dislike => 5 query
    * */
    @Transactional(readOnly = false)
    public void dislike(User user,Post post) {
        Like like = likeRepository.findByUserAndPost(user, post).orElseThrow(LikeNotFoundException::new);
        likeRepository.delete(like);
    }
}
