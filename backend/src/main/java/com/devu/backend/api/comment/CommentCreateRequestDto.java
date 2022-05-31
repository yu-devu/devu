package com.devu.backend.api.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentCreateRequestDto {
    private String username;
    private Long postId;
    private String contents;
    private String parent;
    private Long group;
}
