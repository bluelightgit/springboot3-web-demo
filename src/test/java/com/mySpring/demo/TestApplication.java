package com.mySpring.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mySpring.demo.Models.NewsES;
import com.mySpring.demo.Recommendation.Recommendation;
import com.mySpring.demo.Services.NewsESService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/*
 * springboot测试类
 */
import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Services.NewsService;
import com.mySpring.demo.Services.VisitorService;

@SpringBootTest
public class TestApplication {

    @Autowired
    private NewsService newsService;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private NewsESService newsESService;

    @Test
    public void testNewsService() throws IOException {
        News news = new News();
        news.setId(1L);
        news.setTitle("testTitle");
        news.setContent("a".repeat(10007));
        news.setUrl("testUrl");
        news.setPublishTime(0L);
        news.setTag("testTag");
        news.setImageUrl("https://www.google.co.jp/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        newsService.createNews(news);


    }

    @Test
    public void TestVisitorService() throws IOException {

        Visitor visitor = new Visitor();
        visitor.setId(0L);
        visitor.setIpAddress("0.0.0.0");
        visitor.setTimeStamp(1L);
        visitor.setDeviceType("pc");
        visitor.setNewsId(725L);
        // String testUUID = new UUIDFunction().setUUID();
        visitor.setUUID("testUUID");
        visitorService.createVisitor(visitor);

    }

    @Autowired
    private Recommendation recommendation;

    @Test
    public void TestRecommendation() throws IOException {
        String testUUID = "testUUID";
        List<News> newsList = recommendation.getRecommendedNews(testUUID);
        for (News news : newsList) {
            System.out.println(news.getTitle());
        }
    }

    @Test
    public void DeleteDuplicateNews() {
        newsService.deleteDuplicateNews();
    }


    /**
     * 测试Elasticsearch是否连接
     */
    @Test
    public void TestElasticsearch() {
        NewsES newsES = new NewsES();
        newsES.setId(1L);
        newsES.setTitle("testTitle");
        newsES.setContent("test".repeat(100));
        newsES.setUrl("testUrl");
        newsES.setPublishTime(0L);
        newsES.setTag("testTag");
        newsES.setImageUrl("https://www.google.co.jp/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        newsES.setViews(0L);
        newsESService.createNewsES(newsES);

    }
    @Test
    public void TestDeleteES() {
        newsESService.deleteNewsES(1L);
    }
    /**
     * 测试Elasticsearch的NewsES类与NewsESRepository的search方法
     * 以及同步MySQL数据到Elasticsearch
     */
    @Test
    public void TestNewsES() {
        List<News> newsList = newsService.getAllNews();
        for (News news : newsList) {
            NewsES newsES = new NewsES();
            newsES.setId(news.getId());
            newsES.setTitle(news.getTitle());
            newsES.setContent(news.getContent());
            newsES.setUrl(news.getUrl());
            newsES.setImageUrl(news.getImageUrl());
            newsES.setTag(news.getTag());
            newsES.setPublishTime(news.getPublishTime());
            newsES.setViews(news.getViews());
            newsESService.createNewsES(newsES);
        }

        List<NewsES> newsESList = newsESService.findByTitleOrContent("openai", "openai");
        for (NewsES newsES : newsESList) {
            System.out.println(newsES.getTitle());
        }
    }

    
}
