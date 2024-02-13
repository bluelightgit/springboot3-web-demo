package com.mySpring.demo.recommendation.controller;

import com.mySpring.demo.models.news.pojos.NewsES;
import com.mySpring.demo.recommendation.common.Recommendation;
import com.mySpring.demo.services.impl.NewsESService;
import com.mySpring.demo.services.impl.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class RecommendationController {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private NewsESService newsESService;

    @Autowired
    private Recommendation recommendation;

    // 获取用户的新闻推荐
    @GetMapping("/visitor/{UUID}/recommendation")
    public List<NewsES> getUserRecommendation(@PathVariable String UUID) throws IOException {
//        List<Visitor> history = visitorService.getVisitorByUUID(UUID);
        if (!visitorService.isBlankVisitor(UUID)) {
            return recommendation.getRecommendedNews(UUID, false);
        } else {
            return newsESService.getHottestOfWeek();
        }
    }
}
