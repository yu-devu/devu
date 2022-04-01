package com.devu.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      //패턴
                .allowedOrigins("*")    //URL
                .allowedOrigins("http://localhost:8080","http://54.180.29.69:8080") //URL
                .allowedHeaders("header1","header2")  //header
                .allowedMethods("GET","POST");        //method
    }
}
