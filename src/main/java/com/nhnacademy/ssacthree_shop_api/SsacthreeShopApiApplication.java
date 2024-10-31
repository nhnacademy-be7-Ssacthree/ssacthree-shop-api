package com.nhnacademy.ssacthree_shop_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SsacthreeShopApiApplication {

   public static void main(String[] args) {
        SpringApplication.run(SsacthreeShopApiApplication.class, args);
    }

}
