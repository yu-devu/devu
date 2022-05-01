package com.devu.backend.controller.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    private List<MultipartFile> images = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
}
