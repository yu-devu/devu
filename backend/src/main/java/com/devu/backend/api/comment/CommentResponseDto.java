package com.devu.backend.api.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentResponseDto {
    private String username;
    private String title;
    private Long commentId;
    private String contents;
    private Long group;
    private String parent;
}
