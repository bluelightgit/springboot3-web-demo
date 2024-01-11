package com.mySpring.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType; // Add this import statement


// Generated by https://start.springboot.io

@SpringBootApplication
@ComponentScan(basePackages = {}//{"com.mySpring.demo.Controllers", "com.mySpring.demo.Services", "com.mySpring.demo.Repositories", "com.mySpring.demo.Models", "com.mySpring.demo.Utils", "com.mySpring.demo.Interfaces", "com.mySpring.demo.CORSFilter", "com.mySpring.demo.TestSwagger"},
	, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.mySpring.demo.AMQP.*"))
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
