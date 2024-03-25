package com.mySpring.demo.recommendation.feignClient.fallBack;

import com.mySpring.demo.models.news.pojos.NewsES;
import com.mySpring.demo.recommendation.feignClient.NewsESServiceClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewsESServiceClientFallBack implements NewsESServiceClient {
    @Override
    public List<NewsES> getHottestOfWeek() {
        return new ArrayList<>();
    }

    @Override
    public NewsES getNewsById(Long Id) {
        return null;
    }

    @Override
    public List<NewsES> getAllNews() {
        return new ArrayList<>();
    }
}
