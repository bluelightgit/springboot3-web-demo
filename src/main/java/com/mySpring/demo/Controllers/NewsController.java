package com.mySpring.demo.Controllers;

import java.io.IOException;
import java.util.List;


import com.mySpring.demo.Recommendation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.mySpring.demo.Models.News;
import com.mySpring.demo.Services.NewsService;
import com.mySpring.demo.Services.VisitorService;
import com.mySpring.demo.Utils.CookieFunction;
import com.mySpring.demo.Utils.UUIDFunction;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @CrossOrigin
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private Recommendation recommendation;

    @GetMapping
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public News getNews(@PathVariable Long id) {
        return newsService.getNews(id);
    }

    @PostMapping
    public News createNews(@RequestBody News news) {
        return newsService.createNews(news);
    }

    @PutMapping("/{id}")
    public News updateNews(@PathVariable Long id, @RequestBody News news) {
        return newsService.updateNews(id, news);
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
    }

    @GetMapping("/recommendation")
    public List<News> getRecommendation() {
        return newsService.getHotestOfToday();
    }

    // 记录用户阅读新闻的行为
    // @PostMapping("/visitor/{UUID}/read/{newsId}")
    // public void recordUserReadNews(@PathVariable String UUID, @PathVariable Long newsId) {
    //     newsService.recordUserReadNews(UUID, newsId);
    // }

    // 获取用户的新闻推荐
    @GetMapping("/visitor/{UUID}/recommendation")
    public List<News> getUserRecommendation(@PathVariable String UUID) throws IOException {
//        List<Visitor> history = visitorService.getVisitorByUUID(UUID);

        return recommendation.getRecommendedNews(UUID);
    }

    // @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    @RequestMapping("/getUUID")
    public String getVisitorUUID(HttpServletRequest request, HttpServletResponse response) {
        CookieFunction cookieFunction = new CookieFunction();
        String uuid = cookieFunction.getCookie(request);
        if (uuid == null || visitorService.checkUUID(uuid) == false) {
            UUIDFunction uuidFunction = new UUIDFunction();
            uuid = uuidFunction.setUUID();
            cookieFunction.setCookie(response, uuid);
        }
        return uuid;

    }
    
    @PostMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }
    
}
