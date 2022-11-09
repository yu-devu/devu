package com.devu.backend.service;

import com.devu.backend.api.position.PositionDto;
import com.devu.backend.api.position.PositionResponseDto;
import com.devu.backend.entity.CompanyType;
import com.devu.backend.entity.Position;
import com.devu.backend.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    @Transactional
    public void collectNaver() {
        int startNum = 1;
        int endNum = 10;
        while (true) {
            String url = "https://career.navercorp.com/naver/job/listJson";
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

                Position position = Position.builder()
                        .link(link)
                        .title(removeEmoji(title))
                        .company(CompanyType.NAVER)
                        .duration(duration)
                        .build();

                positionRepository.save(position);
            }
            startNum += 10;
            endNum += 10;
        }
    }

    @Transactional
    public void collectBaemin(int page) {
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

                String link = "https://career.woowahan.com/recruitment/"+ recruitNumber +
                        "/detail?category=jobGroupCodes%3ABA005001&keyword=&jobCodes=&employmentTypeCodes=";
                String duration = openDate + " ~ 영입 종료시";
                Position position = Position.builder()
                        .link(link)
                        .title(removeEmoji(title))
                        .company(CompanyType.BAEMIN)
                        .duration(duration)
                        .build();

                positionRepository.save(position);
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
            return cnt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public void collectKakao(int page) {
        Document document = null;
        String url = "https://careers.kakao.com/jobs?company=ALL&keyword=&page="+ page;
        Connection con = Jsoup.connect(url);
        try {
            document = con.get();
            Elements links = document.select(".list_jobs li .link_jobs");
            Elements titles = document.select(".list_jobs li .tit_jobs");
            Elements durations = document.select(".list_jobs li .list_info > dd:first-of-type");
            for (int i =0; i < titles.size(); i++) {
                String link = "https://careers.kakao.com" + links.get(i).attr("href");
                String title = titles.get(i).text();
                String duration = durations.get(i).text();

                Position position = Position.builder()
                        .link(link)
                        .title(removeEmoji(title))
                        .company(CompanyType.KAKAO)
                        .duration(duration)
                        .build();

                positionRepository.save(position);
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
            return cnt+1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public void collectLine() {
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

                Position position = Position.builder()
                        .link(link)
                        .title(removeEmoji(title))
                        .company(CompanyType.LINE)
                        .duration(duration)
                        .build();

                positionRepository.save(position);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void collectCoupang(int page) {
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

                Position position = Position.builder()
                        .link(link)
                        .title(removeEmoji(title))
                        .company(CompanyType.COUPANG)
                        .duration(duration)
                        .build();

                positionRepository.save(position);
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
            return cnt+1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void collectAllPosition() {
        positionRepository.deleteAll();
        //Naver 크롤링
        collectNaver();
        //Baemin 크롤링
        int cnt = getBaeminPage();
        for (int i =0; i <= cnt; i++)
            collectBaemin(i);
        //Kakao 크롤링
        cnt = getKakaoPage();
        for (int i =1; i <= cnt; i++)
            collectKakao(i);
        // Line 크롤링
        collectLine();
        // Coupang 크롤링
        cnt = getCoupangPage();
        for (int i = 1; i <= cnt; i++)
            collectCoupang(i);
    }

    public PositionDto getAllPosition(String keyword, Pageable pageable) {
        return getPositionDto(getPositionResponseDto(keyword, pageable));
    }

    private Page<PositionResponseDto> getPositionResponseDto(String keyword, Pageable pageable) {
        return getPosition(keyword, pageable)
                .map(PositionResponseDto::new);
    }

    private Page<Position> getPosition(String keyword, Pageable pageable) {
        return positionRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    private PositionDto getPositionDto(Page<PositionResponseDto> allPosition) {
        return PositionDto.builder().size(allPosition.getTotalElements())
                .positions(allPosition.getContent())
                .build();
    }

    public PositionDto getNaver(String keyword, Pageable pageable) {
        return getPositionDto(getPositionResponseDtoByCompany(keyword, pageable, CompanyType.NAVER));
    }

    private Page<PositionResponseDto> getPositionResponseDtoByCompany(String keyword, Pageable pageable, CompanyType company) {
        return getPositionByCompany(keyword, pageable, company).map(PositionResponseDto::new);
    }

    private Page<Position> getPositionByCompany(String keyword, Pageable pageable, CompanyType company) {
        return positionRepository.findByTitleContainingIgnoreCaseAndCompany(keyword, company, pageable);
    }

    public PositionDto getKakao(String keyword, Pageable pageable) {
        return getPositionDto(getPositionResponseDtoByCompany(keyword, pageable, CompanyType.KAKAO));
    }

    public PositionDto getLine(String keyword, Pageable pageable) {
        return getPositionDto(getPositionResponseDtoByCompany(keyword, pageable, CompanyType.LINE));
    }

    public PositionDto getCoupang(String keyword, Pageable pageable) {
        return getPositionDto(getPositionResponseDtoByCompany(keyword, pageable, CompanyType.COUPANG));
    }

    public PositionDto getBaemin(String keyword, Pageable pageable) {
        return getPositionDto(getPositionResponseDtoByCompany(keyword, pageable, CompanyType.BAEMIN));
    }

    public static String removeEmoji(String input){
        if(input == null)return null;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (i < (input.length() - 1)) {
                if (Character.isSurrogatePair(input.charAt(i), input.charAt(i + 1))) {
                    i += 1;
                    continue;
                }
            }
            sb.append(input.charAt(i));
        }
        return sb.toString();
    }
}
