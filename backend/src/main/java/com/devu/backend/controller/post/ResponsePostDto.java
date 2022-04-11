package com.devu.backend.controller.post;

import com.devu.backend.entity.post.QuestionStatus;
import com.devu.backend.entity.post.StudyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePostDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private Long hit;
    private Long like;
    private StudyStatus studyStatus;
    private QuestionStatus questionStatus;
}
