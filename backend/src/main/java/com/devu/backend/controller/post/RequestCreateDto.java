package com.devu.backend.controller.post;

import com.devu.backend.entity.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCreateDto {
    private String username;
    private String title;
    private String content;
    //image,tag 추후에 필요
}
