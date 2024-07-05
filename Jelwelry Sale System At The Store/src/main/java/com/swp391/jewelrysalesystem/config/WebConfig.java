package com.swp391.jewelrysalesystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // .allowedOrigins("http://localhost:3000") // Chỉ định các origin cho phép truy cập, hoặc "*" cho phép tất cả
                .allowedOrigins("https://jewelry-sale-system.vercel.app/") // Chỉ định các origin cho phép truy cập, hoặc "*" cho phép tất cả
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Phương thức HTTP cho phép
                .allowedHeaders("*") // Các header cho phép
                .allowCredentials(true); // Cho phép sử dụng cookie khi truy cập từ origin khác
    }
}