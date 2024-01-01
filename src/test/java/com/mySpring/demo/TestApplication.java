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
    public void testServices() throws IOException {
        News news = new News();
        news.setId(0L);
        news.setTitle("testTitle");
        news.setContent("testContent");
        news.setUrl("testUrl");
        news.setPublishTime(0L);
        List<String> tag = new ArrayList<>();
        tag.add("testTag");
        news.setTag(tag);
        // byte[] image = ImageTransformer.encodeImageToBase64("src\\main\\resources\\testImage.png");
        news.setImageUrl("src\\main\\resources\\testImage.png");
        newsService.createNews(news);

        Visitor visitor = new Visitor();
        visitor.setDeviceInfo("testDeviceInfo");
        visitor.setIpAddress("0.0.0.0");
        visitor.setTimeStamp(0L);
        visitor.setDeviceType("pc");
        visitor.setNewsId(0L);
        visitorService.createVisitor(visitor);

    }

    
}
