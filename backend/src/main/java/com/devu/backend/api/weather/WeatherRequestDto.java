package com.devu.backend.api.weather;

import lombok.Getter;

@Getter
public class WeatherRequestDto {
    private String baseDate; //ex) 220513
    private String baseTime; //ex) 0200
    /*
    * baseTime 가능한 시간 -> 앞뒤로 1시간 30분으로 나누기
    * 0200 -> 00:30 ~ 03:30
    * 0500 -> 03:30 ~ 06:30
    * 0800 -> 06:30 ~ 09:30
    * 1100 -> 09:30 ~ 12:30
    * 1400 -> 12:30 ~ 15:30
    * 1700 -> 15:30 ~ 18:30
    * 2000 -> 18:30 ~ 21:30
    * 2300 -> 21:30 ~ 00:30
    * */
}
