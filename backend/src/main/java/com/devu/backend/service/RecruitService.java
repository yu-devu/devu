package com.devu.backend.service;

import com.devu.backend.entity.CompanyType;
import com.devu.backend.entity.Recruit;
import com.devu.backend.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    @Transactional
    public void getNaver() {
        int startNum = 1;
        int endNum = 10;
        while (true) {
            String url = "https://recruit.navercorp.com/naver/job/listJson";
            Document doc = null;
            try {
                doc = Jsoup.connect(url)
                        .header("origin", "https://recruit.navercorp.com")
                        .header("referer", "Referer: https://recruit.navercorp.com/naver/job/list/developer")
                        .header("accept-encoding", "gzip, deflate, br")
                        .data("classNm", "developer")
                        .data("startNum", Integer.toString(startNum))
                        .data("endNum", Integer.toString(endNum))
                        .ignoreContentType(true)
                        .post();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray(doc.text());
            if (jsonArray.length() == 0)
                break;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String start = jsonObject.getString("staYmd");
                String end = jsonObject.getString("endYmd");
                String annoId = Integer.toString(jsonObject.getInt("annoId"));
                String title =  jsonObject.getString("jobNm");

                String link = "https://recruit.navercorp.com/naver/job/detail/developer?annoId=" + annoId +
                        "&classId=&jobId=&entTypeCd=&searchTxt=&searchSysComCd=";
                String duration = start + " ~ " + end;
                log.info("link: {}, date: {}, title: {}", link, duration, title);

                Recruit recruit = Recruit.builder()
                        .link(link)
                        .title(title)
                        .company(CompanyType.NAVER)
                        .duration(duration)
                        .build();

                recruitRepository.save(recruit);
            }
            startNum += 10;
            endNum += 10;
        }
    }

    @Transactional
    public void getBaemin(int page) {
        try {
            String url = "https://career.woowahan.com/w1/recruits?category=jobGroupCodes%3ABA005001&" +
                    "recruitCampaignSeq=0&jobGroupCodes=BA005001&page=" + page + "&size=21&sort=updateDate%2Cdesc";
            Document doc = Jsoup.connect(url)
                    .header("origin", "https://career.woowahan.com")
                    .header("referer", "https://career.woowahan.com/?category=jobGroupCodes%3ABA005001&keyword=&jobCodes=&employmentTypeCodes=")
                    .header("accept-encoding", "gzip, deflate, br")
                    .ignoreContentType(true)
                    .get();
            JSONObject jsonObject = new JSONObject(doc.text());
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONArray lists = dataObject.getJSONArray("list");
            for (int i = 0; i < lists.length(); i++) {
                String recruitNumber = lists.getJSONObject(i).getString("recruitNumber");
                String openDate = lists.getJSONObject(i).getString("recruitOpenDate").substring(0, 10);
                String title = lists.getJSONObject(i).getString("recruitName");
                log.info("link: {}, date: {}, title: {}", recruitNumber, openDate, title);

                String link = "https://career.woowahan.com/recruitment/"+ recruitNumber +
                        "/detail?category=jobGroupCodes%3ABA005001&keyword=&jobCodes=&employmentTypeCodes=";
                String duration = openDate + " ~ 영입 종료시";
                Recruit recruit = Recruit.builder()
                        .link(link)
                        .title(title)
                        .company(CompanyType.BAEMIN)
                        .duration(duration)
                        .build();

                recruitRepository.save(recruit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBaeminPage() {
        String url = "https://career.woowahan.com/w1/recruits?category=jobGroupCodes%3ABA005001&recruitCampaignSeq=0&jobGroupCodes=BA005001&page=0&size=21&sort=updateDate%2Cdesc";
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .header("origin", "https://career.woowahan.com")
                    .header("referer", "https://career.woowahan.com/?category=jobGroupCodes%3ABA005001&keyword=&jobCodes=&employmentTypeCodes=")
                    .header("accept-encoding", "gzip, deflate, br")
                    .ignoreContentType(true)
                    .get();
            JSONObject jsonObject = new JSONObject(doc.text());
            JSONObject dataObject = jsonObject.getJSONObject("data");
            int cnt = dataObject.getInt("totalSize") / dataObject.getInt("pageSize");
            log.info("cnt: {}", cnt);
            return cnt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public void getKakao(int page) {
        Document document = null;
        String url = "https://careers.kakao.com/jobs?company=ALL&keyword=&page="+ page;
        Connection con = Jsoup.connect(url);
        try {
            document = con.get();
            Elements links = document.select(".list_jobs li .link_jobs");
            Elements titles = document.select(".list_jobs li .tit_jobs");
            Elements durations = document.select(".list_jobs li .list_info > dd:first-of-type");
            for (int i =0; i < titles.size(); i++) {
                String link = links.get(i).attr("href");
                String title = titles.get(i).text();
                String duration = durations.get(i).text();
                log.info("link: {}, title: {}, duration: {}", link, title, duration);

                Recruit recruit = Recruit.builder()
                        .link(link)
                        .title(title)
                        .company(CompanyType.KAKAO)
                        .duration(duration)
                        .build();

                recruitRepository.save(recruit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getKakaoPage() {
        String url = "https://careers.kakao.com/jobs?company=ALL&keyword=&page=1";
        Document document = null;
        Connection con = Jsoup.connect(url);
        try {
            document = con.get();
            Elements num = document.select(".link_job1 .emph_num");
            int limit = 15;
            int cnt = Integer.parseInt(num.text()) / limit;
            log.info("cnt: {}", cnt);
            return cnt+1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public void getLine() {
        Document document = null;
        String url = "https://careers.linecorp.com/ko/jobs?ca=All&ci=Seoul,Bundang&co=East%20Asia";
        Connection con = Jsoup.connect(url);
        try {
            document = con.get();
            Elements links = document.select(".job_list li a");
            Elements titles = document.select(".job_list li .title");
            Elements durations = document.select(".job_list li .date");
            for (int i =0; i < titles.size(); i++) {
                String link = "https://careers.linecorp.com/" + links.get(i).attr("href");
                String title = titles.get(i).text();
                if(title.endsWith(" NEW"))
                    title = title.substring(0, title.length()-4);
                String duration = durations.get(i).text();
                log.info("link: {}, title: {}, duration: {}", link, title, duration);

                Recruit recruit = Recruit.builder()
                        .link(link)
                        .title(title)
                        .company(CompanyType.LINE)
                        .duration(duration)
                        .build();

                recruitRepository.save(recruit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void getCoupang(int page) {
        Document document = null;
        String url = "https://www.coupang.jobs/kr/jobs/?" + page + "department=Ecommerce+Engineering&department=Play" +
                "+Engineering&department=Product+UX&department=Search+and+Discovery&department=Search+and" +
                "+Discovery+Core+Infrastructure&department=Cloud+Platform&department=Corporate+IT&department=eCommerce" +
                "+Product&department=FTS+(Fulfillment+and+Transportation+System)&department=Marketplace%2c+Catalog+%26" +
                "+Pricing+Systems&department=Program+Management+Office&department=Customer+Experience+Product#results";
        Connection con = Jsoup.connect(url);
        try {
            document = con.get();
            Elements links = document.select(".job-listing .card-title a");
            Elements titles = document.select(".job-listing .card-title .stretched-link");
            for (int i =0; i < titles.size(); i++) {
                String link = "https://www.coupang.jobs" + links.get(i).attr("href");
                String title = titles.get(i).text();
                String duration = "공고 확인";
                log.info("link: {}, title: {}, duration: {}", link, title, duration);

                Recruit recruit = Recruit.builder()
                        .link(link)
                        .title(title)
                        .company(CompanyType.COUPANG)
                        .duration(duration)
                        .build();

                recruitRepository.save(recruit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCoupangPage() {
        String url = "https://www.coupang.jobs/kr/jobs/?department=Ecommerce+Engineering&department=Play" +
                "+Engineering&department=Product+UX&department=Search+and+Discovery&department=Search+and" +
                "+Discovery+Core+Infrastructure&department=Cloud+Platform&department=Corporate+IT&department=eCommerce" +
                "+Product&department=FTS+(Fulfillment+and+Transportation+System)&department=Marketplace%2c+Catalog+%26" +
                "+Pricing+Systems&department=Program+Management+Office&department=Customer+Experience+Product#results";
        Document document = null;
        Connection con = Jsoup.connect(url);
        try {
            document = con.get();
            Element num = document.select(".job-count strong").get(2);
            int limit = 20;
            int cnt = Integer.parseInt(num.text()) / limit;
            log.info("cnt: {}", cnt);
            return cnt+1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
//    @Scheduled(cron = "0 0 4 * * *")  매일 새벽4시마다 실행(혹시몰라 잠시중단)
    public void getAllRecruit() {
        recruitRepository.deleteAll();
        //Naver 크롤링
        getNaver();
        //Baemin 크롤링
        int cnt = getBaeminPage();
        for (int i =0; i <= cnt; i++)
            getBaemin(i);
        //Kakao 크롤링
        cnt = getKakaoPage();
        for (int i =1; i <= cnt; i++)
            getKakao(i);
        // Line 크롤링
        getLine();
        // Coupang 크롤링
        cnt = getCoupangPage();
        for (int i = 1; i <= cnt; i++)
            getCoupang(i);
    }
}
