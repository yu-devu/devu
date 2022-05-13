package com.devu.backend.api.weather;

import com.devu.backend.controller.ResponseErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class WeatherApiController {

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestBody WeatherRequestDto dto) {
        try {
            String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
                    "?serviceKey=PyNY0qeRt39Rj07xn2QCs%2BokqrxCfg%2FJkw0RaONPBX3GybvBQjznoLKfITYrjhDTz7bKwND%2BozBbbqHwS89T7Q%3D%3D" +
                    "&dataType=JSON" +
                    "&numOfRows=11" +
                    "&pageNo=1" +
                    "&base_date=" +
                    dto.getBaseDate()+
                    "&base_time=" +
                    dto.getBaseTime()+
                    "&nx=92" +
                    "&ny=90";
            String res = getResultString(apiUrl);
            HashMap<String, Object> resultMap = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            resultMap = objectMapper.readValue(res, HashMap.class);
            return ResponseEntity.ok(resultMap);
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    private String getResultString(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return String.valueOf(sb);
    }
}
