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
                .allowedOrigins("https://jewelry-sale-system.vercel.app") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Ensure all needed methods are included
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (cookies, etc.)
    }
}
