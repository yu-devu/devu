package com.devu.backend.repository.comment;

import com.devu.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryExtension {
    List<Comment> findAllByPostIdOrderByCreateAt(Long postId);

    void deleteById(Long commentId);

    long countByGroupNum(Long groupNum);

    Optional<Comment> findByUserId(Long id);
}
