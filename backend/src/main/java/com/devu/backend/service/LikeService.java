package com.devu.backend.service;

import com.devu.backend.common.exception.LikeNotFoundException;
import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.common.exception.UnlikedPostException;
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

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //dislike query 감소용 container
    private final ConcurrentHashMap<User, Post> container = new ConcurrentHashMap<>();

    /*
    * like => 4 query
    * */
    @Transactional(readOnly = false)
    public void addLike(String username, Long postId) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        container.put(user, post);
        //이미 좋아요가 되어있으면 좋아요 해제
        if (isAlreadyLiked(user,post)){
            throw new UnlikedPostException();
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

    /*
    * dislike => 5 query
    * */
    @Transactional(readOnly = false)
    public void dislike() {
        Enumeration<User> keys = container.keys();
        User user = keys.nextElement();
        Post post = container.get(user);
        Like like = likeRepository.findByUserAndPost(user, post).orElseThrow(LikeNotFoundException::new);
        likeRepository.delete(like);
    }
}
