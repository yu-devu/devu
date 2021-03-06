package com.devu.backend.repository.post;

import com.devu.backend.entity.post.QuestionStatus;
import com.devu.backend.entity.post.StudyStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostSearch {
    private String order;//정렬 방식
    private String sentence;//검색 단어
    private List<Long> tagId = new ArrayList<>();//태그 필터링
    private StudyStatus studyStatus;//상태
    private QuestionStatus questionStatus;//상태
}
