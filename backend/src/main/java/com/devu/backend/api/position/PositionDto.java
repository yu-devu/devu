package com.devu.backend.api.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto {
    private long size;
    private List<PositionResponseDto> positions = new ArrayList<>();
}
