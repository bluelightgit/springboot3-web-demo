package com.mySpring.demo.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.mySpring.demo.Models.NewsES;
import com.mySpring.demo.Recommendation.*;
import com.mySpring.demo.Services.NewsESService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.mySpring.demo.Services.VisitorService;
import com.mySpring.demo.Utils.CookieFunction;
import com.mySpring.demo.Utils.UUIDFunction;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsESService newsESService;
    @Autowired
    private VisitorService visitorService;

    @Autowired
    private Recommendation recommendation;

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @GetMapping
    public List<NewsES> getAllNews() {
        List<NewsES> allNews = new ArrayList<>();
        newsESService.getAllNewsES().forEach(allNews::add);
        return allNews;
    }

    @GetMapping("/{id}")
    public NewsES getNews(@PathVariable Long id) {
        return newsESService.getNewsES(id);
    }

    @PostMapping
    public NewsES createNews(@RequestBody NewsES news) {
        return newsESService.createNewsES(news);
    }

    @PutMapping("/{id}")
    public NewsES updateNews(@PathVariable Long id, @RequestBody NewsES news) {
        return newsESService.updateNewsES(id, news);
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable Long id) {
        newsESService.deleteNewsES(id);
    }

    @GetMapping("/hottest")
    public List<NewsES> getRecommendation() {
        List<NewsES> news = newsESService.getHottestOfWeek();
        if (news == null || news.isEmpty()) {
            logger.warn("Hot news not found");
            return null;
        }
        return newsESService.getHottestOfWeek();
    }


    // 获取用户的新闻推荐
    @GetMapping("/visitor/{UUID}/recommendation")
    public List<NewsES> getUserRecommendation(@PathVariable String UUID) throws IOException {
//        List<Visitor> history = visitorService.getVisitorByUUID(UUID);
        if (visitorService.checkUUID(UUID)) {
            return recommendation.getRecommendedNews(UUID);
        } else {
            return newsESService.getHottestOfWeek();
        }
    }

    @RequestMapping("/getUUID")
    public String getVisitorUUID(HttpServletRequest request, HttpServletResponse response) {
        CookieFunction cookieFunction = new CookieFunction();
        String uuid = cookieFunction.getCookie(request);
        if (uuid == null || visitorService.checkUUID(uuid) == false) {
            UUIDFunction uuidFunction = new UUIDFunction();
            uuid = uuidFunction.setUUID();
            logger.info("Create new UUID: [{}]", uuid);
            cookieFunction.setCookie(response, uuid);
        }
        return uuid;

    }

    @PostMapping("/addView")
    public void addView(@RequestBody Long id) {
        newsESService.increaseViews(id);
    }

    @PostMapping("/addView/{id}")
    public void addView2(@PathVariable Long id) {
        newsESService.increaseViews(id);
    }

    @PostMapping("/search/{keyword}")
    public List<NewsES> searchNews(@PathVariable String keyword) {
        return newsESService.findByTitleOrContent(keyword, keyword);
    }

    @PostMapping("/upload")
    public void uploadNews(@RequestBody NewsES news) {
        newsESService.addUploadNews(news);
    }
    @PostMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }
    
}
