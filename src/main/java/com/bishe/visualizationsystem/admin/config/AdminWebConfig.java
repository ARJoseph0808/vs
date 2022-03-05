package com.bishe.visualizationsystem.admin.config;

import com.bishe.visualizationsystem.admin.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Joseph
 * @date 2021/12/22
 * @apiNote
 * 1、编写一个拦截器实现HandlerInterceptor接口
 * 2、拦截器注册到容器中(实现WebMvcConfigurer的addInterceptors)
 * 3、指定拦截器规则【如果是拦截所有，静态资源也会拦截】
 */
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**","/sql","/kaptcha");

    }
}
