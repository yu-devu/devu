package com.devu.backend.controller;

import com.devu.backend.service.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/crawling")
public class RecruitController {

    private final RecruitService recruitService;

    @GetMapping("/naver")
    public void getNaver() {
        recruitService.getNaver();
    }

    @GetMapping("/baemin")
    public void getBaemin() {
        int cnt = recruitService.getBaeminPage();
        for (int i =0; i <= cnt; i++)
            recruitService.getBaemin(i);
    }

    @GetMapping("/kakao")
    public void getKakao() {
        int cnt = recruitService.getKakaoPage();
        for (int i =1; i <= cnt; i++)
            recruitService.getKakao(i);
    }

    @GetMapping("/line")
    public void getLine() {
        recruitService.getLine();
    }

    @GetMapping("/coupang")
    public void getCoupang() {
        int cnt = recruitService.getCoupangPage();
        for (int i = 1; i <= cnt; i++)
            recruitService.getCoupang(i);
    }
}
