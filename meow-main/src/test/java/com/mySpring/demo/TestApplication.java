package com.mySpring.demo;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mySpring.demo.langchain.SentimentExtracting;
import com.mySpring.demo.models.news.dtos.UserUploadedNews;
import com.mySpring.demo.models.news.pojos.NewsES;

import com.mySpring.demo.services.impl.NewsESService;
import com.mySpring.demo.services.impl.UserUploadNewsService;
import dev.langchain4j.model.openai.OpenAiChatModel;
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
        List<UserUploadedNews> tempNews = userUploadNewsService.getAllTempNews();
        for (UserUploadedNews news : tempNews) {
            System.out.println(news.getTitle());
        }
        try {
            userUploadNewsService.changeStatus(tempNews.get(0), 1);
            System.out.println("change status to approved success");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            userUploadNewsService.submitTempNews();
            System.out.println("submit tempNews success");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestViewsFunctions() throws JsonProcessingException {
        userUploadNewsService.setMaxId();
        newsESService.increaseViews(userUploadNewsService.maxId.get());
        newsESService.updateViews();
        System.out.println(newsESService.getViews(userUploadNewsService.maxId.get()));
    }

    @Test
    public void TestDeleteES() {
        userUploadNewsService.setMaxId();
        newsESService.deleteNewsES(userUploadNewsService.maxId.get());
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

    @Autowired
    private OpenAiChatModel chatLanguageModel;

    @Test
    public void TestSentimentExtracting() {
        String content = "fucking nigger";
        SentimentExtracting sentimentExtracting = new SentimentExtracting(chatLanguageModel);
        if (sentimentExtracting.isInvalidSentiment(content)) {
            System.out.println("Invalid content");
        } else {
            System.out.println("Valid content");
        }
    }

}
