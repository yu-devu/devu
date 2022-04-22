package com.devu.backend.service;

import com.devu.backend.common.exception.CommentContentNullException;
import com.devu.backend.common.exception.CommentNotFoundException;
import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.controller.comment.CommentCreateRequestDto;
import com.devu.backend.controller.comment.CommentResponseDto;
import com.devu.backend.controller.comment.CommentUpdateRequestDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Post;
import com.devu.backend.repository.CommentRepository;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Comment saveComment(CommentCreateRequestDto requestDto) {
        User user = userRepository.getById(requestDto.getUserId());
        Post post = postRepository.getById(requestDto.getPostId());
        if (requestDto.getContents() == null) {
            throw new CommentContentNullException();
        }
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .contents(requestDto.getContents())
                .build();
        return commentRepository.save(comment);
    }

    public Comment updateComment(CommentUpdateRequestDto updateRequestDto) {
        Comment comment = commentRepository.findById(updateRequestDto.getCommentId()).orElseThrow(CommentNotFoundException::new);
        comment.updateContent(updateRequestDto.getContent());
        return comment;
    }
}
