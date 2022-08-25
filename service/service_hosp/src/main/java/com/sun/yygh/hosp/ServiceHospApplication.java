package com.sun.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-21 18:09
 **/
@SpringBootApplication
@ComponentScan(basePackages = "com.sun")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.sun")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
