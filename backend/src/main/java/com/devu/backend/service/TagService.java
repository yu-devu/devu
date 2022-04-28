package com.devu.backend.service;

import com.devu.backend.api.tag.TagResponseDto;
import com.devu.backend.common.exception.PostNotFoundException;
import com.devu.backend.entity.Tag;
import com.devu.backend.entity.post.Post;
import com.devu.backend.entity.post.PostTags;
import com.devu.backend.repository.PostRepository;
import com.devu.backend.repository.TagRepository;
import com.devu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    @Transactional
    public TagResponseDto saveTags(List<String> tags, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<Tag> tagList = tags.stream().map(s
                -> Tag.builder()
                .postTags(PostTags.valueOf(s.toUpperCase()))
                .post(post)
                .build()
        ).collect(Collectors.toList());
        tagRepository.saveAll(tagList);
        tagList.forEach(t -> t.changePost(post));//연관관계 편의 메서드
        return TagResponseDto
                .builder()
                .postId(post.getId())
                .tagIds(tagList.stream().map(Tag::getId).collect(Collectors.toList()))
                .tags(tagList.stream().map(Tag::getPostTags).collect(Collectors.toList()))
                .build();
    }

    /*
    * 1) 태그 수 자체가 늘어나는 경우 && 태그가 변경되는 경우
    * 2) 태그 수 자체가 줄어드는 경우 && 태그가 변경되는 경우
    * 3) 태그 수는 동일하고 태그가 변경되는 경우
    * 4) 태그 수만 늘어나는 경우
    * 5) 태그 수만 줄어드는 경우
    * */
    @Transactional
    public void deleteTags(List<Tag> tags) {
        for (Tag tag : tags) {
            log.info("remove Tag : {}",tag.getPostTags());
            log.info("remove Tag Post Id : {}",tag.getPost().getId());
            tagRepository.deleteById(tag.getId());
        }
    }
}
