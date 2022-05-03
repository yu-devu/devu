package com.devu.backend.service;

import com.devu.backend.entity.CompanyType;
import com.devu.backend.entity.Recruit;
import com.devu.backend.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
    public void getNaver(String url) {
        driver.get(url) ;
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

    private void quitDriver() {
        driver.quit(); // webDriver 종료
    }

}
