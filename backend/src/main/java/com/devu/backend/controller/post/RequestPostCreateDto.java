package com.devu.backend.controller.post;

import com.devu.backend.entity.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestPostCreateDto {
    private String username;
    private String title;
    private String content;
    //image,tag 추후에 필요
}
