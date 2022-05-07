package com.devu.backend.entity.post;

import com.devu.backend.entity.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;


@Entity
@Getter
@DiscriminatorValue("Q")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Post{
    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @Builder
    public Question(Long id, User user, String title,
                    String content, Long hit, List<Like> likes,
                    List<Image> images, List<Comment> comments,
                    List<PostTag> tags, QuestionStatus qnaStatus) {
        super(id, user, title, content, hit, likes, comments,images,tags);
        this.questionStatus = qnaStatus;
    }

    public void updateStatus(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;
    }
}
