package com.mySpring.demo.recommendation.feignClient;

import com.mySpring.demo.models.news.pojos.NewsES;
import com.mySpring.demo.recommendation.feignClient.fallBack.NewsESServiceClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "main-server", url = "http://localhost:8085", fallback = NewsESServiceClientFallBack.class)
public interface NewsESServiceClient {
    @GetMapping("/news/hottest")
    List<NewsES> getHottestOfWeek();

    @GetMapping("/news/{Id}")
    NewsES getNewsById(@PathVariable("Id") Long Id);

    @GetMapping("/news")
    List<NewsES> getAllNews();
}
