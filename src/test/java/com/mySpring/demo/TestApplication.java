package com.mySpring.demo;

import java.io.IOException;
import java.util.List;

import com.mySpring.demo.Models.NewsES;
import com.mySpring.demo.Recommendation.Recommendation;
import com.mySpring.demo.Repositories.NewsESRepository;
import com.mySpring.demo.Services.NewsESService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/*
 * springboot测试类
 */
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Services.VisitorService;

@SpringBootTest
public class TestApplication {


    @Autowired
    private VisitorService visitorService;

    @Autowired
    private NewsESService newsESService;


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
        List<NewsES> newsList = recommendation.getRecommendedNews(testUUID);
        for (NewsES news : newsList) {
            System.out.println(news.getTitle());
        }
    }


    /**
     * 测试Elasticsearch是否连接
     */
    @Test
    public void TestElasticsearch() {
        NewsES newsES = new NewsES();
        newsES.setId(0L);
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
        newsESService.deleteNewsES(0L);
    }

}
