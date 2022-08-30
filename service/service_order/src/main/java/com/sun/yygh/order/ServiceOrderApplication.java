package com.sun.yygh.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 19:55
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com.sun"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.sun"})
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}
