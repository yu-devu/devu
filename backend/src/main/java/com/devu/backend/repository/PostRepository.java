package com.devu.backend.repository;

import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Post;
import com.devu.backend.entity.post.Question;
import com.devu.backend.entity.post.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where TYPE(p) IN(Chat)")
    Page<Chat> findAllChats(Pageable pageable);

    @Query("select p from Post p where TYPE(p) IN(Study)")
    Page<Study> findAllStudies(Pageable pageable);

    @Query("select p from Post p where TYPE(p) IN(Question)")
    Page<Question> findAllQuestions(Pageable pageable);

    Optional<Chat> findChatById(Long id);

    Optional<Question> findQuestionById(Long id);

    Optional<Study> findStudyById(Long id);
}
