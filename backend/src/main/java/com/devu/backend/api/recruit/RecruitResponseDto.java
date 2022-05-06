package com.devu.backend.api.recruit;

import com.devu.backend.entity.Recruit;
import lombok.Getter;

@Getter
public class RecruitResponseDto {

    private String company;
    private String title;
    private String duration;
    private String link;

    public RecruitResponseDto(Recruit recruit) {
        this.company = recruit.getCompany().toString();
        this.title = recruit.getTitle();
        this.duration = recruit.getDuration();
        this.link = recruit.getLink();
    }
}
