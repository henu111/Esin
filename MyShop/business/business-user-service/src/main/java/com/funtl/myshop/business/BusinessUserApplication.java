package com.funtl.myshop.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BusinessUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessUserApplication.class, args);
    }
}
