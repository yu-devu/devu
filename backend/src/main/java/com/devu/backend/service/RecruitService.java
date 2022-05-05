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
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    private WebDriver driver;
    public static String WEB_DRIVER_ID = "webdriver.chrome.driver"; // Properties 설정
    public static String WEB_DRIVER_PATH = "C:/chromedriver.exe"; // WebDriver 경로

    @PostConstruct
    public void init() {
        chrome();
    }

    private void chrome() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // webDriver 옵션 설정.
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--lang=ko");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.setCapability("ignoreProtectedModeSettings", true);

        // webDriver 생성.
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    @Transactional
    public void getNaver() {
        driver.get("https://recruit.navercorp.com/naver/job/list/developer") ;
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);  // 페이지 불러오는 여유시간.

        Long lastHeight = (Long)(((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight"));
        Long newHeight = 0L;
        try {
            while (true){
                log.info("{}, {}", newHeight, lastHeight);
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
                if (!driver.findElement(By.cssSelector("div#moreDiv")).getAttribute("style").equals("display: none;")) {
                    driver.findElement(By.cssSelector("div#moreDiv")).click();
                }
                Thread.sleep(500);
                newHeight = (Long)(((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight"));

                if (newHeight.equals(lastHeight))
                    break;
                lastHeight = newHeight;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> elements = driver.findElements(By.cssSelector("#jobListDiv ul li"));
        int idx = 0;
        for (WebElement element : elements) {
            idx++;
            log.info("----------" + idx + "----------------");
            String link = element.findElement(By.cssSelector("a")).getAttribute("href");
            log.info("link: {}", link);
            String title = element.findElement(By.cssSelector(".crd_tit")).getText();
            log.info("title: {}", title);
            String duration = element.findElement(By.cssSelector(".crd_date")).getText();
            log.info("duration: {}", duration);

            Recruit recruit = Recruit.builder()
                    .link(link)
                    .title(title)
                    .company(CompanyType.NAVER)
                    .duration(duration)
                    .build();

            recruitRepository.save(recruit);
        }

        quitDriver();
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

    private void quitDriver() {
        driver.quit(); // webDriver 종료
    }

}
