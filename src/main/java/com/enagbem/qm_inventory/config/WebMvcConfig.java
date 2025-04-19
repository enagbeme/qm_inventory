package com.enagbem.qm_inventory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URLs with pattern /uploads/** to the physical uploads directory
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Configure resource handler for static reports
        registry.addResourceHandler("/static/reports/**")
                .addResourceLocations("classpath:/static/reports/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}