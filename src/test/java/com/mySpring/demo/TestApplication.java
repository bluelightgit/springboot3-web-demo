package com.mySpring.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/*
 * springboot测试类
 */
import com.mySpring.demo.Utils.ImageTransformer;
import com.mySpring.demo.Utils.UUIDFunction;
import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Models.VisitorHistory;
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
        visitor.setDeviceInfo(0L);
        visitor.setIpAddress("0.0.0.0");
        visitor.setTimeStamp(1L);
        visitor.setDeviceType("pc");
        visitor.setNewsId(1L);
        // List<VisitorHistory> historyList = new ArrayList<>();
        // VisitorHistory visitorHistory = new VisitorHistory();
        // visitorHistory.addTimeAndNews(1L, 1L);
        // historyList.add(visitorHistory);
        // visitor.setHistory(historyList);
        String testUUID = new UUIDFunction().setUUID();
        visitor.setUUID(testUUID);
        visitorService.createVisitor(visitor);

    }

    @Test
    public void TestRecommendation() throws IOException {
        Long NowTimeStamp = System.currentTimeMillis();
        System.out.println(NowTimeStamp);
    }

    
}
