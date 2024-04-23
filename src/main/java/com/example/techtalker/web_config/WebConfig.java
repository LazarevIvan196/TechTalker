package com.example.techtalker.web_config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        registry.addResourceHandler("/bootstrap/**")
                .addResourceLocations("classpath:/static/bootstrap/");

        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/FAQ").setViewName("FAQ");
    }
}
