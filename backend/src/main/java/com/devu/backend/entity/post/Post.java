package com.devu.backend.entity.post;

import com.devu.backend.controller.post.PostRequestUpdateDto;
import com.devu.backend.entity.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Post extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    @Column(name = "hit_count")
    private Long hit;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    //cascadeType.ALL 설정해보기
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();//tags는 index와 상관없이 자주 변경 될 수 있어서 LinkedList


    //==비지니스 로직==//
    public void plusHit() {
        this.hit++;
    }

    public boolean tagUpdateValidation(PostRequestUpdateDto updateDto) {
        List<PostTags> savedTags = getSavedTags();
        List<PostTags> updateTags = getUpdateTags(updateDto);
        return (savedTags.size() != updateTags.size()) || !savedTags.containsAll(updateTags);
    }

    public void updatePost(PostRequestUpdateDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
    }

    public List<Tag> updateWithTags(PostRequestUpdateDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        List<PostTags> savedTags = getSavedTags();
        List<PostTags> updateTags = getUpdateTags(updateDto);
        addTags(savedTags, updateTags);
        return removeTags(savedTags, updateTags);
    }

    private void addTags(List<PostTags> savedTags, List<PostTags> updateTags) {
        List<PostTags> add = updateTags.stream().filter(t -> !savedTags.contains(t)).collect(Collectors.toList());
        List<Tag> addTags = add.stream().map(a -> Tag.builder()
                .postTags(a)
                .post(this)
                .build()
        ).collect(Collectors.toList());
        for (Tag addTag : addTags) {
            this.getTags().add(addTag);
        }
    }

    //saveTag 중에 updateTag에 없으면 객체에서 빼자
    private List<Tag> removeTags(List<PostTags> savedTags, List<PostTags> updateTags) {
        List<PostTags> remove = savedTags.stream().filter(t -> !updateTags.contains(t)).collect(Collectors.toList());
        List<Tag> removeTags = remove.stream().map(r -> Tag.builder()
                .postTags(r)
                .post(this)
                .build()
        ).collect(Collectors.toList());
        for (Tag removeTag : removeTags) {
            this.getTags().remove(removeTag);
        }
        return removeTags;
    }


    private List<PostTags> getUpdateTags(PostRequestUpdateDto updateDto) {
        return updateDto.getTags()
                .stream().map(s -> PostTags.valueOf(s.toUpperCase()))
                .collect(Collectors.toList());
    }

    private List<PostTags> getSavedTags() {
        return this.getTags()
                .stream().map(Tag::getPostTags)
                .collect(Collectors.toList());
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setPost(this);
    }

    //<--연관관계 편의 메서드-->//
    public void setUser(User user) {
        if (this.user != null) {
            this.user.getPosts().remove(this);
        }
        this.user = user;
        user.getPosts().add(this);
    }
}
