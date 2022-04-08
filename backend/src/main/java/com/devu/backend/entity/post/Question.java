package com.devu.backend.entity.post;

import com.devu.backend.entity.Image;
import com.devu.backend.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Set;

@DiscriminatorValue("Q")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Post{
    @Enumerated(EnumType.STRING)
    private QuestionStatus qnaStatus;

    @Builder

    public Question(Long id, User user, String title, String content, Long hit, Long recommendation, Set<Image> images, QuestionStatus qnaStatus) {
        super(id, user, title, content, hit, recommendation, images);
        this.qnaStatus = qnaStatus;
    }
}
