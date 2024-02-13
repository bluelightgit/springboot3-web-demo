package com.mySpring.demo.services;

import com.mySpring.demo.models.news.pojos.NewsES;

import java.util.List;

public interface INewsESService {
    Iterable<NewsES> getAllNewsES();
    NewsES getNewsES(Long id);
    NewsES createNewsES(NewsES newsES);
    void deleteNewsES(Long id);
    NewsES updateNewsES(Long id, NewsES newsESDetails);
    List<NewsES> findByTitle(String title);
    List<NewsES> findByTitleOrContent(String title, String content);

}
