package com.mySpring.demo.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;


@SpringBootApplication
@EnableFeignClients(basePackages = {"com.mySpring.demo.recommendation"})
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class RecommendationApp {

    public static void main(String[] args) {
        SpringApplication.run(com.mySpring.demo.recommendation.RecommendationApp.class, args);
    }

}
