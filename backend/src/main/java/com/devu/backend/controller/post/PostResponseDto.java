package com.devu.backend.controller.post;

import com.devu.backend.api.comment.CommentResponseDto;
import com.devu.backend.entity.Comment;
import com.devu.backend.entity.post.QuestionStatus;
import com.devu.backend.entity.post.StudyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private List<String> url;
    private Long hit;
    private Integer like;
    private StudyStatus studyStatus;
    private QuestionStatus questionStatus;
    private int commentsSize;//get All 게시글 시 사용
    private List<CommentResponseDto> comments;// 추후 CommentResponseDto로 변경 필요
    private List<String> tags;
    private LocalDateTime createAt;
    private LocalDateTime lastModifiedAt;
}
