package com.mySpring.demo.controllers;

import com.fasterxml.jackson.core.JacksonException;
import com.mySpring.demo.models.news.dtos.UserUploadedNews;
import com.mySpring.demo.services.impl.NewsESService;
import com.mySpring.demo.services.impl.UserUploadNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private NewsESService newsESService;

    @Autowired
    private UserUploadNewsService userUploadNewsService;

    @GetMapping("/uploadNewsList")
    public List<UserUploadedNews> getUploadedNews() throws JacksonException {
        return userUploadNewsService.getAllTempNews();
    }

    @GetMapping("/uploadNewsList/{id}")
    public UserUploadedNews getUploadedNewsById(@PathVariable Long id) throws JacksonException {
        return userUploadNewsService.getTempNewsById(id);
    }

    @PutMapping("/uploadNewsList/{id}")
    public ResponseEntity<?> updateNewsStatus(@PathVariable long id, @RequestBody int status) throws JacksonException {
        return userUploadNewsService.changeStatus(userUploadNewsService.getTempNewsById(id), status);
    }

    @PostMapping("/submitNews")
    public ResponseEntity<?> submitNews(@RequestBody long id) throws JacksonException {
        return userUploadNewsService.submitTempNews();
    }

    
}
