package com.devu.backend.entity.post;

import com.devu.backend.entity.Image;
import com.devu.backend.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Set;

@Entity
@DiscriminatorValue("S")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends Post{
    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    @Builder

    public Study(Long id, User user, String title, String content, Long hit, Long recommendation, Set<Image> images, StudyStatus studyStatus) {
        super(id, user, title, content, hit, recommendation, images);
        this.studyStatus = studyStatus;
    }
}
