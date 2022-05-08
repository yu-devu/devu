package com.devu.backend.api.position;

import com.devu.backend.entity.Position;
import lombok.Getter;

@Getter
public class PositionResponseDto {

    private String company;
    private String title;
    private String duration;
    private String link;

    public PositionResponseDto(Position position) {
        this.company = position.getCompany().toString();
        this.title = position.getTitle();
        this.duration = position.getDuration();
        this.link = position.getLink();
    }
}
