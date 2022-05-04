package com.devu.backend.controller;

import com.devu.backend.entity.CompanyType;
import com.devu.backend.entity.Recruit;
import com.devu.backend.repository.RecruitRepository;
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
    private final RecruitRepository recruitRepository;

    //selenium
    @GetMapping("/naver")
    public void getNaver() {
        String url = "https://recruit.navercorp.com/naver/job/list/developer";

//        Connection con = Jsoup.connect(url);
//        Document document = null;
//        try {
//            document = con.get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        recruitService.getNaver(url);
    }

    //selenium
    @GetMapping("/baemin")
    public void getBaemin() {
        int cnt = recruitService.getBaeminPage();
        for (int i =0; i <= cnt; i++)
            recruitService.getBaemin(i);
    }


}
