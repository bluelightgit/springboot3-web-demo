package com.mySpring.demo.Interfaces;


import java.util.List;

import com.mySpring.demo.Models.News;

public interface INewsService {
    List<News> getAllNews();
    News getNews(Long id);
    News createNews(News news);
    News updateNews(Long id, News newsDetails);
    void deleteNews(Long id);
}