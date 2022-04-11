package com.devu.backend.entity;

import com.devu.backend.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class Image extends BaseTime{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String path;

    private String name;

    public void setPost(Post post) {
        this.post = post;
    }
}
