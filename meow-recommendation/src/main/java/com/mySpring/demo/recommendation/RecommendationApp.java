package com.mySpring.demo.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RecommendationApp {

    public static void main(String[] args) {
        SpringApplication.run(com.mySpring.demo.recommendation.RecommendationApp.class, args);
    }

}
