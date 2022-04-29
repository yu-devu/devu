package com.devu.backend.entity.post;

import com.devu.backend.entity.*;
import lombok.*;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("S")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends Post{
    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    @Builder
    public Study(Long id, User user, String title,
                 String content, Long hit,
                 List<Like> likes, List<Image> images, List<Comment> comments,
                 List<Tag> tags, StudyStatus studyStatus) {
        super(id, user, title, content, hit, likes, comments,images,tags);
        this.studyStatus = studyStatus;
    }

    public void updateStatus(StudyStatus studyStatus) {
        this.studyStatus = studyStatus;
    }
}
