package com.jcertpre.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ✅ Bộ lọc UTF-8
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    // ✅ Đăng ký Interceptor kiểm tra đăng nhập admin
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminAuthInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin/login",          // cho phép truy cập trang login
                        "/api/login/admin",      // cho phép xử lý login
                        "/css/**", "/js/**", "/images/**" // tài nguyên tĩnh
                );
    }
}
