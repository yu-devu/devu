package com.devu.backend.entity.post;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("Q")
@Entity
public class QnA extends Post{
    private QnAStatus qnaStatus;
}
