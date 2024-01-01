package com.mySpring.demo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mySpring.demo.Repositories.NewsRepository;
import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Interfaces.INewsService;
@Service
public class NewsService implements INewsService {

    @Autowired
    private NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getNews(Long id) {
        Optional<News> news = newsRepository.findById(id);
        return news.orElse(null);
    }

    public News createNews(News news) {
        return newsRepository.save(news);
    }

    public News updateNews(Long id, News newsDetails) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if(optionalNews.isPresent()) {
            News news = optionalNews.get();
            news.setId(newsDetails.getId());
            news.setUrl(newsDetails.getUrl());
            news.setTitle(newsDetails.getTitle());
            news.setTag(newsDetails.getTag());
            news.setImageUrl(newsDetails.getImageUrl());
            news.setContent(newsDetails.getContent());
            news.setPublishTime(newsDetails.getPublishTime());
            return newsRepository.save(news);
        } else {
            return null;
        }
    }

    public void deleteNews(Long id) {    
        newsRepository.deleteById(id);
    }
    
    // public List<News> getRecommendation(Visitor visitor) {
    //     // 获取用户浏览过的新闻
    //     List<Long> newsIdList = new ArrayList<>();
    //     for (Long newsId : visitor.getHistory()) {
    //         newsIdList.add(newsId);
    //     }
    //     return newsRepository.getRecommendation();
    // }
}
