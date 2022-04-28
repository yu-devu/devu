package com.devu.backend.entity;

import com.devu.backend.entity.post.Post;
import com.devu.backend.entity.post.PostTags;

import javax.persistence.*;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PostTags postTags;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /*
    * 연관관계 편의 메서드
    * */
    void changePost(Post post){
        this.post = post;
        this.post.getTags().add(this);
    }

}
