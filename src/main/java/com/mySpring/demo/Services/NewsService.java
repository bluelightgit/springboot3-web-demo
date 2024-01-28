package com.mySpring.demo.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.mySpring.demo.Models.NewsES;
import com.mySpring.demo.Recommendation.Recommendation;

import com.mySpring.demo.Repositories.NewsESRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mySpring.demo.Repositories.NewsRepository;
import com.mySpring.demo.Repositories.VisitorRepository;
import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Interfaces.INewsService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsService implements INewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private Map<Long, Integer> viewsMap = new ConcurrentHashMap<>();

    @Cacheable(value = "allNews")
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    @Cacheable(value = "news", key = "#id")
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
     * 增加一次浏览量
     */
    public void addView(Long id) {
        News news = getNews(id);
        if (news != null) {
            viewsMap.put(id, viewsMap.getOrDefault(id, 0) + 1);
        }
    }

    /*
     * 批量更新views到数据库
     */
    @Transactional
    public void updateViewsToDB() {
        if (!viewsMap.isEmpty()) {
            logger.info("Updating views to database, {} items", viewsMap.size());
        }
        for (Map.Entry<Long, Integer> entry : viewsMap.entrySet()) {
            newsRepository.updateViews(entry.getKey(), entry.getValue());
        }
        viewsMap.clear();
    }
    @Transactional
    @Scheduled(fixedRate = 1000)  // 每秒执行一次
    public void scheduledUpdateViews() {
        updateViewsToDB();
    }
    /*
     * 获取一段时间内浏览量最高的10篇新闻(解决冷启动问题)
     */
//    @Cacheable(value = "hottestNews", key = "#root.methodName")
    public List<News> getHottestOfWeek() {
        long NowTimeStamp = System.currentTimeMillis() / 1000;
        return newsRepository.findTopNByPublishTimeBetweenOrderByViewsDesc(1L, NowTimeStamp, 20);
    }


    /*
     * 查找重复的新闻 by title
     */
    public List<News> getDuplicateNews() {
        return newsRepository.findAllDuplicateNews();
    }

    public void deleteDuplicateNews() {
        List<News> duplicateNews = getDuplicateNews();
        for (News news : duplicateNews) {
            newsRepository.delete(news);
        }
    }
}
