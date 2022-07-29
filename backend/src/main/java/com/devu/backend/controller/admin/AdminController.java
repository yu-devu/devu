package com.devu.backend.controller.admin;

import com.devu.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    private ResponseEntity adminHome() {
        return ResponseEntity.ok().body("어드민 홈 테스트");
    }
}
