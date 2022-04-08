package com.devu.backend.entity.post;

import com.devu.backend.entity.BaseTime;
import com.devu.backend.entity.Image;
import com.devu.backend.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    private Long hit;

    private Long recommendation;

    @OneToMany(mappedBy = "post")
    private Set<Image> images = new HashSet<>();
}
