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
    //추후 추가 필요
}
