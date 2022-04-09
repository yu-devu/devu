package com.devu.backend.entity;

import com.devu.backend.entity.post.Post;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String path;

    private String name;

    private String save_name;

    private Long fileSize;

    private LocalDateTime create_date_time;
}
