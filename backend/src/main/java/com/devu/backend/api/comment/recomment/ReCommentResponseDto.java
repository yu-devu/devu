package com.devu.backend.api.comment.recomment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReCommentResponseDto {
    private String username;
    private String title;
    private Long commentId;
    private Long group;
    private String parent;
}
