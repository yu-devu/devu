package com.devu.backend.entity;

import com.devu.backend.entity.post.Post;
import com.devu.backend.entity.post.PostTags;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PostTags postTags;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void updateTag(PostTags tag) {
        this.postTags = tag;
    }

    /*
    * 연관관계 편의 메서드
    * */
    public void changePost(Post post){
        this.post = post;
        this.post.getTags().add(this);
    }

}
