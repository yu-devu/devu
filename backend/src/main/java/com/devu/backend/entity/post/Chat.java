package com.devu.backend.entity.post;

import com.devu.backend.entity.Image;
import com.devu.backend.entity.User;
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
    public Chat(Long id, User user, String title, String content, Long hit, Long like, List<Image> images) {
        super(id, user, title, content, hit, like, images);
    }
}
