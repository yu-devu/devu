package com.devu.backend.repository;

import com.devu.backend.entity.Tag;
import com.devu.backend.entity.post.PostTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByPostIdAndPostTags(Long id, PostTags postTags);
}
