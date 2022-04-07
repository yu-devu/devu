package com.devu.backend.entity.post;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("S")
public class Study extends Post{
    private StudyStatus studyStatus;

}
