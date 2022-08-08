package com.devu.backend.controller.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SumDTO {
    private Long sumOfUsers;
    private Long sumOfPosts;
    private Long sumOfStudies;
    private Long sumOfQuestions;
    private Long sumOfChats;
}
