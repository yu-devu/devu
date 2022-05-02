package com.devu.backend.api.status;

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
public class ResponseStatusDto {
    private Long id;
    private QuestionStatus questionStatus;
    private StudyStatus studyStatus;
}
