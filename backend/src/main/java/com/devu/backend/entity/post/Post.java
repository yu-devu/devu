package com.devu.backend.entity.post;

import com.devu.backend.common.exception.LikeZeroException;
import com.devu.backend.controller.post.RequestPostUpdateDto;
import com.devu.backend.entity.BaseTime;
import com.devu.backend.entity.Image;
import com.devu.backend.entity.Like;
import com.devu.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    //==비지니스 로직==//
    public void plusHit() {
        this.hit++;
    }

    public void updatePost(RequestPostUpdateDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
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
