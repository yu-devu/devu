package com.devu.backend.api.weather;

import com.devu.backend.controller.ResponseErrorDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
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

@Slf4j
@RestController
@RequestMapping("/api")
public class WeatherApiController {

    /*
    * 고정된 시간대는 동일한 객체 반환하도록 설계 변경
    * */
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
            JSONObject jsonObject = getJsonObjectByObjectMapperFromString(apiUrl);
            JSONArray jsonArray = getJsonArray(jsonObject);
            JSONObject reformedData = getReformedData(jsonArray);
            return ResponseEntity.ok(reformedData.toString());
        }catch (Exception e){
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    private JSONObject getReformedData(JSONArray jsonArray) {
        HashMap<Object, Object> reformedMap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Object category = object.get("category");
            Object fcstValue = object.get("fcstValue");
            reformedMap.put(category, fcstValue);
        }
        JSONObject reformedData = new JSONObject();
        reformedData.put("reformed", reformedMap);
        return reformedData;
    }

    private JSONArray getJsonArray(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject
                .getJSONObject("rawData")
                .getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items")
                .getJSONArray("item");
        return jsonArray;
    }

    private JSONObject getJsonObjectByObjectMapperFromString(String apiUrl) throws IOException {
        String resStr = getResultString(apiUrl);
        HashMap<String, Object> resultMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        resultMap = objectMapper.readValue(resStr, HashMap.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rawData", resultMap);
        return jsonObject;
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
