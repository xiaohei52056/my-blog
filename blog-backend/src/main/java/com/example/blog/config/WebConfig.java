package com.example.blog.config;

import com.example.blog.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**") // 拦截所有 /api 开头的请求
                .excludePathPatterns("/api/users/login", "/api/users/register") // 排除登录注册
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**"); // 排除接口文档
        // 文章接口不再全局排除，由拦截器内部根据请求方法决定是否需要认证
    }
}
