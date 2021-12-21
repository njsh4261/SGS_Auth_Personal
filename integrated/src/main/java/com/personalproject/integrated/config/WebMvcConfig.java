package com.personalproject.integrated.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final WithoutTokenInterceptor withoutTokenInterceptor;
    private final WithTokenInterceptor withTokenInterceptor;

    @Autowired
    public WebMvcConfig(WithTokenInterceptor withTokenInterceptor, WithoutTokenInterceptor withoutTokenInterceptor) {
        this.withTokenInterceptor = withTokenInterceptor;
        this.withoutTokenInterceptor = withoutTokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(withoutTokenInterceptor).addPathPatterns(
                "/signin", "/signup"
        );
        registry.addInterceptor(withTokenInterceptor).addPathPatterns(
                "/admin"
        );
    }
}
