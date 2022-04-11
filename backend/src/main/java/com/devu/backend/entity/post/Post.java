package com.devu.backend.entity.post;

import com.devu.backend.common.exception.LikeZeroException;
import com.devu.backend.controller.post.RequestPostUpdateDto;
import com.devu.backend.entity.BaseTime;
import com.devu.backend.entity.Image;
import com.devu.backend.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DiscriminatorColumn(name = "dtype")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Post extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    @Column(name = "hit_count")
    private Long hit;

    /*
    * like column 그대로 쓰면 오류 발생 => like_count로 변경
    * */
    @Column(name = "like_count")
    private Long like;

    @OneToMany(
            mappedBy = "post",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Image> images = new ArrayList<>();

    //==비지니스 로직==//
    public void plusHit() {
        this.hit++;
    }

    public void plusRecommendation() {
        this.like++;
    }

    public void minusRecommendation() {
        if (this.like == 0) {
            throw new LikeZeroException();
        }
        this.like++;
    }

    public void updatePost(RequestPostUpdateDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setPost(this);
    }
}
