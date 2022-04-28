package com.devu.backend.controller.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestUpdateDto {
    private String title;
    private String content;
    private String status;
    private List<MultipartFile> images;
    private List<String> tags;
}
