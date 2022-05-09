package com.devu.backend.api.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TagResponseDto {
    private List<Long> tagIds = new ArrayList<>();
    private Long postId;
}
