package com.henau.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MallWaraApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallWaraApplication.class, args);
    }

}
