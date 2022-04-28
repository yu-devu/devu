package com.devu.backend.entity.post;

import com.devu.backend.entity.*;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;


@Entity
@Getter
@DiscriminatorValue("C")
@NoArgsConstructor
public class Chat extends Post{

    @Builder
    public Chat(Long id, User user, String title,
                String content, Long hit, List<Like> likes,
                List<Image> images, List<Comment> comments,
                List<Tag> tags) {
        super(id, user, title, content, hit, likes, comments,images,tags);
    }
}

