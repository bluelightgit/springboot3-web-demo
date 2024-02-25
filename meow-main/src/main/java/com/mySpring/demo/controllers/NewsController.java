package com.mySpring.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mySpring.demo.models.news.dtos.UserUploadedNews;
import com.mySpring.demo.models.news.dtos.ViewUpdate;
import com.mySpring.demo.models.news.pojos.NewsES;
import com.mySpring.demo.services.impl.NewsESService;
import com.mySpring.demo.services.impl.UserUploadNewsService;
import com.mySpring.demo.services.impl.ViewsUpdateProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.mySpring.demo.services.impl.VisitorService;
import com.mySpring.demo.utils.CookieFunction;
import com.mySpring.demo.utils.UUIDFunction;

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
    private UserUploadNewsService userUploadNewsService;

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

    @GetMapping("/{id}/views")
    public Long getViews(@PathVariable Long id) throws JsonProcessingException {
        return newsESService.getViews(id);
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

    @RequestMapping("/getUUID")
    public String getVisitorUUID(HttpServletRequest request, HttpServletResponse response) {
        CookieFunction cookieFunction = new CookieFunction();
        String uuid = cookieFunction.getCookie(request);
        if (uuid == null || visitorService.isBlankVisitor(uuid)) {
            UUIDFunction uuidFunction = new UUIDFunction();
            uuid = uuidFunction.setUUID();
            logger.info("Create new UUID: [{}]", uuid);
            cookieFunction.setCookie(response, uuid);
        }
        return uuid;

    }

    @Value("${kafka.topic.views-update-topic}")
    private String viewsUpdateTopic;

    private final ViewsUpdateProducerService viewsUpdateProducerService;

    public NewsController(ViewsUpdateProducerService viewsUpdateProducerService) {
        this.viewsUpdateProducerService = viewsUpdateProducerService;
    }
    @PostMapping("/addView")
    public void addView(@RequestBody Long id) throws JsonProcessingException {
        newsESService.increaseViews(id);
//        viewsUpdateProducerService.sendViewsUpdateMessage(viewsUpdateTopic, new ViewUpdate(id, 1L));
    }

    @PostMapping("/addView/{id}")
    public void addView2(@PathVariable Long id) throws JsonProcessingException {
        newsESService.increaseViews(id);
//        viewsUpdateProducerService.sendViewsUpdateMessage(viewsUpdateTopic, new ViewUpdate(id, 1L));
    }

    @PostMapping("/search/{keyword}")
    public List<NewsES> searchNews(@PathVariable String keyword) {
        return newsESService.findByTitleOrContent(keyword, keyword);
    }

    @PostMapping("/upload")
    public void uploadNews(@RequestBody UserUploadedNews userUploadedNews) throws JacksonException {
        userUploadNewsService.addToTempNews(userUploadedNews);
    }
    @PostMapping("/test-request")
    public ResponseEntity<String> testPostRequest() {
        return ResponseEntity.ok("POST request successful");
    }
    
}
