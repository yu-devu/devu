package com.devu.backend.repository;

import com.devu.backend.entity.post.Chat;
import com.devu.backend.entity.post.Post;
import com.devu.backend.entity.post.Question;
import com.devu.backend.entity.post.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryExtension {
    Page<Study> findAllStudy(Pageable pageable, PostSearch postSearch);
    Page<Chat> findAllChat(Pageable pageable, PostSearch postSearch);
    Page<Question> findAllQuestion(Pageable pageable, PostSearch postSearch);
}
