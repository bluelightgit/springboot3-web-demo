package com.mySpring.demo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mySpring.demo.Repositories.NewsRepository;
import com.mySpring.demo.Repositories.VisitorRepository;
import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Interfaces.INewsService;
@Service
public class NewsService implements INewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private VisitorRepository visitorRepository;

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
            news.setViews(newsDetails.getViews());
            return newsRepository.save(news);
        } else {
            return null;
        }
    }

    public void deleteNews(Long id) {    
        newsRepository.deleteById(id);
    }

    /*
     * 获取24h内浏览量最高的10篇新闻(解决冷启动问题)
     */
    public List<News> getHotestOfToday() {
        Long NowTimeStamp = System.currentTimeMillis();
        List<News> hotestOfToday = newsRepository.findTop10ByPublishTimeBetweenOrderByViewsDesc(NowTimeStamp - 86400000, NowTimeStamp);

        return hotestOfToday;
    }
    /*
     * 根据用户浏览历史记录推荐新闻,
     * 该案例中使用文本分类算法进行推荐,
     * 且仅使用新闻title进行分类
     */
    public List<News> getRecommendedNews(Visitor visitor) {
        // 根据用户的历史记录推荐新闻
        String uuid = visitor.getUUID();
        List<Visitor> history = visitorRepository.getHistoryByUUID(uuid);
        List<Long> newsIdList = new ArrayList<>();
        for (Visitor visitorHistory : history) {
            newsIdList.add(visitorHistory.getNewsId());
        }
        // visitor历史记录的详细信息
        List<News> recommendedNews = newsRepository.findAllByIds(newsIdList);
        
        return recommendedNews;
    }
}
