package com.mySpring.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mySpring.demo.Recommendation.Recommendation;
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

    @Test
    public void testNewsService() throws IOException {
        News news = new News();
        news.setId(1L);
        news.setTitle("testTitle");
        news.setContent("a".repeat(10007));
        news.setUrl("testUrl");
        news.setPublishTime(0L);
        // List<String> tag = new ArrayList<>();
        // tag.add("testTag");
        news.setTag("testTag");
        // byte[] image = ImageTransformer.encodeImageToBase64("src\\main\\resources\\testImage.png");
        news.setImageUrl("src\\main\\resources\\testImage.png");
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

    
}
