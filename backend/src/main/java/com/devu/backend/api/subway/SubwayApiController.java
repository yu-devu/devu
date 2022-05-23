package com.devu.backend.api.subway;

import com.devu.backend.controller.ResponseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
public class SubwayApiController {

    @GetMapping("/weekdaySubway")
    public ResponseEntity<?> getWeekdaySubway() {
        try {
            int weekday = 0;
            List<List<String>> allSubwayTime = getSubwayTime();
            List<String> weekdaySubway = allSubwayTime.get(weekday);
            return ResponseEntity.ok(weekdaySubway);
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/weekendSubway")
    public ResponseEntity<?> getWeekendSubway() {
        try {
            int weekend = 1;
            List<List<String>> allSubwayTime = getSubwayTime();
            List<String> weekendSubway = allSubwayTime.get(weekend);
            return ResponseEntity.ok(weekendSubway);
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/holidaySubway")
    public ResponseEntity<?> getHolidaySubway() {
        try {
            int holiday = 2;
            List<List<String>> allSubwayTime = getSubwayTime();
            List<String> holidaySubway = allSubwayTime.get(holiday);
            return ResponseEntity.ok(holidaySubway);
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    private List<List<String>> getSubwayTime() throws IOException {
        Resource resource = new ClassPathResource("subway.csv");
        InputStream subwayInputStream = resource.getInputStream();
        List<List<String>> allSubwayTime = new BufferedReader(new InputStreamReader(subwayInputStream))
                .lines().map(line -> {
                    String[] split = line.split(",");
                    List<String> subwayTime = new ArrayList<>();
                    for (int i = 0; i < split.length; i++) {
                        subwayTime.add(split[i]);
                    }
                    return subwayTime;
                }).collect(Collectors.toList());
        return allSubwayTime;
    }
}
