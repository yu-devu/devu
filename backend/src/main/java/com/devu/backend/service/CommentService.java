package com.devu.backend.service;

import com.devu.backend.api.comment.CommentResponseDto;
import com.devu.backend.common.exception.CommentContentNullException;
import com.devu.backend.common.exception.CommentNotFoundException;
import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.api.comment.CommentCreateRequestDto;
import com.devu.backend.api.comment.CommentUpdateRequestDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.entity.User;
import com.devu.backend.entity.post.Post;
import com.devu.backend.repository.CommentRepository;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment saveComment(CommentCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);
        if (requestDto.getContents() == null) {
            throw new CommentContentNullException();
        }
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .contents(requestDto.getContents())
                .build();
        Comment saveComment = commentRepository.save(comment);
        comment.updateGroup(saveComment.getId());
        return saveComment;
    }

    @Transactional
    public Comment saveReComment(CommentCreateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);
        if (requestDto.getContents() == null) {
            throw new CommentContentNullException();
        }
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .contents(requestDto.getContents())
                .groupNum(requestDto.getGroup())
                .parent(requestDto.getParent())
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(CommentUpdateRequestDto updateRequestDto) {
        Comment comment = commentRepository.findById(updateRequestDto.getCommentId()).orElseThrow(CommentNotFoundException::new);
        comment.updateContent(updateRequestDto.getContents());
        return comment;
    }

    public Page<CommentResponseDto> commentsByPost(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable).map(
                comment -> CommentResponseDto
                        .builder()
                        .username(comment.getUser().getUsername())
                        .title(comment.getPost().getTitle())
                        .commentId(comment.getId())
                        .group(comment.getGroupNum())
                        .contents(comment.getContents())
                        .parent(comment.getParent())
                        .deleted(comment.isDeleted())
                        .build()
        );
    }

    @Transactional
    public void deleteComment(Comment comment) {
        if(commentRepository.countByGroupNum(comment.getGroupNum()) == 1)
            commentRepository.delete(comment);
        else
            comment.updateDeleted();
    }

    @Transactional
    public void deleteReComment(Comment originalComment, Comment comment) {
        if (originalComment.isDeleted()) {
            if (commentRepository.countByGroupNum(comment.getGroupNum()) == 2) {
                commentRepository.delete(originalComment);
            }
        }
        commentRepository.delete(comment);
    }
}
