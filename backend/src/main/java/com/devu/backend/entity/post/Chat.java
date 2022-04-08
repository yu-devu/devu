package com.devu.backend.entity.post;

import com.devu.backend.entity.Image;
import com.devu.backend.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("C")
@Entity
public class Chat extends Post{

    @Builder
    public Chat(Long id, User user, String title, String content, Long hit, Long recommendation, Set<Image> images) {
        super(id, user, title, content, hit, recommendation, images);
    }
}
