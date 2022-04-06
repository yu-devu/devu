package com.devu.backend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    private String content;

    private String postType;

    private Long hit;

    private Long recommendation;

    @OneToMany(mappedBy = "post")
    private Set<Image> images = new HashSet<>();

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;

}
