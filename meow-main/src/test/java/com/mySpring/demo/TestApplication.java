package com.mySpring.demo;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mySpring.demo.models.news.pojos.NewsES;

import com.mySpring.demo.services.impl.NewsESService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/*
 * springboot测试类
 */
import com.mySpring.demo.models.visitor.pojos.Visitor;
import com.mySpring.demo.services.impl.VisitorService;

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


    /**
     * 测试Elasticsearch是否连接
     */
    @Test
    public void TestElasticsearch() throws JsonProcessingException {
        NewsES newsES = new NewsES();
        newsES.setId(9999L);
        newsES.setTitle("testTitle");
        newsES.setContent("test".repeat(100));
        newsES.setUrl("testUrl");
        newsES.setPublishTime(0L);
        newsES.setTag("testTag");
        newsES.setImageUrl("https://www.google.co.jp/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        newsES.setViews(0L);
        newsESService.addUploadNews(newsES);

    }
    @Test
    public void TestDeleteES() {
        newsESService.deleteNewsES(9999L);
    }

    @Test
    public void TestGetMaxId() {
        System.out.println(newsESService.getMaxId());
    }

    @Test
    public void TestGetHottestOfWeek() {
        List<NewsES> newsList = newsESService.getHottestOfWeek();
        for (NewsES news : newsList) {
            System.out.println(news.getTitle());
        }
    }

}
