package com.devu.backend.controller.post;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostCreateDto {
    private String username;
    private String title;
    private String content;
    //image,tag 추후에 필요
}
