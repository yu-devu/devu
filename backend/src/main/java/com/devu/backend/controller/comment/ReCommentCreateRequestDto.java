package com.devu.backend.controller.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReCommentCreateRequestDto {
    private Long userId;
    private Long postId;
    private String contents;
    private String parent;
    private Long group;
}
