package com.devu.backend.repository.comment;

import com.devu.backend.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CommentRepositoryExtension {

    Page<Comment> findByPostId(Long postId, Pageable pageable);
}
