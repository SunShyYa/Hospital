package com.sun.yygh.user.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-28 12:00
 **/
@Configuration
@MapperScan("com.sun.yygh.user.mapper")
public class UserConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
