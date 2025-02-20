package com.dreamers2025.dct.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//전역 크로스 오리진 설정: 허용할 클라이언트 설정
@Configuration
public class CrossOriginConfig implements WebMvcConfigurer {
    private String[] urls = {
            "http://localhost:5173",
    };
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry
                .addMapping("/api/**")
                .allowedOrigins(urls)
                .allowedMethods("*")
                .allowedHeaders("*")
        ;
    }
}
