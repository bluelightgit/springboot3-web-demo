package com.mySpring.demo;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mySpring.demo.models.news.dtos.UserUploadedNews;
import com.mySpring.demo.models.news.pojos.NewsES;

import com.mySpring.demo.services.impl.NewsESService;
import com.mySpring.demo.services.impl.UserUploadNewsService;
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

    @Autowired
    private UserUploadNewsService userUploadNewsService;


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
    public void TestUploadToElasticsearch() throws JacksonException {
        UserUploadedNews userUploadedNews = new UserUploadedNews();
        userUploadedNews.setId(9999L);
        userUploadedNews.setTitle("test");
        userUploadedNews.setContent("test");
        userUploadedNews.setUrl("test");
        userUploadedNews.setImageUrl("test");
        userUploadedNews.setTag("test");
        userUploadedNews.setPublishTime(9999L);
        userUploadedNews.setViews(9999L);
        userUploadedNews.setUserId(0L);
        userUploadedNews.setUsername("testUsername");
        userUploadedNews.setStatus(0);
        try {
            userUploadNewsService.addToTempNews(userUploadedNews);
            System.out.println("add to tempNews success");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<UserUploadedNews> tempNews = userUploadNewsService.getTempNews();
        for (UserUploadedNews news : tempNews) {
            System.out.println(news.getTitle());
        }
        try {
            userUploadNewsService.changeStatus(tempNews.get(0), 2);
            System.out.println("change status to reject success");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            userUploadNewsService.submitTempNews(tempNews);
            System.out.println("submit tempNews success");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void TestDeleteES() {
        newsESService.deleteNewsES(9999L);
    }

    @Test
    public void TestGetMaxId() {
        userUploadNewsService.setMaxId();
        System.out.println(userUploadNewsService.maxId.get());
    }

    @Test
    public void TestGetHottestOfWeek() {
        List<NewsES> newsList = newsESService.getHottestOfWeek();
        for (NewsES news : newsList) {
            System.out.println(news.getTitle());
        }
    }

}
