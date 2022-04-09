package com.devu.backend.controller.post;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPostUpdateDto {
    private String title;
    private String content;
    private String status;
    //image,tag 추후에 필요
}
