package com.devu.backend.api.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentUpdateRequestDto {
    private Long commentId;
    private String content;
}
