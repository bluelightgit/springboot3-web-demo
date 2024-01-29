package com.mySpring.demo.Services;


import com.mySpring.demo.Interfaces.INewsESService;
import com.mySpring.demo.Models.NewsES;
import com.mySpring.demo.Repositories.NewsESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class NewsESService implements INewsESService {

    @Autowired
    private NewsESRepository newsESRepository;
    private BlockingQueue<NewsES> uploadNews = new LinkedBlockingQueue<>();
    private ConcurrentHashMap<Long, Integer> viewsMap = new ConcurrentHashMap<>();

    public NewsES getNewsES(Long id) {
        Optional<NewsES> newsES = newsESRepository.findById(id);
        return newsES.orElse(null);
    }

    public Iterable<NewsES> getAllNewsES() {
        return newsESRepository.findAll();
    }

    public List<NewsES> findByTitle(String title) {
        return newsESRepository.findByTitle(title);
    }

    public List<NewsES> findByTitleOrContent(String title, String content) {
        return newsESRepository.findByTitleOrContent(title, content);
    }

    public List<NewsES> findByTitleOrContentWithPagination(String title, String content) {
        return newsESRepository.findByTitleOrContent(title, content);
    }

    public NewsES createNewsES(NewsES newsES) {
        return newsESRepository.save(newsES);
    }

    public void deleteNewsES(Long id) {
        newsESRepository.deleteById(id);
    }
    public NewsES updateNewsES(Long id, NewsES newsESDetails) {
        Optional<NewsES> optionalNewsES = newsESRepository.findById(id);
        NewsES newsES = optionalNewsES.orElse(null);
        if (newsES == null) {
            return null;
        }
        newsES.setTitle(newsESDetails.getTitle());
        newsES.setContent(newsESDetails.getContent());
        newsES.setUrl(newsESDetails.getUrl());
        newsES.setImageUrl(newsESDetails.getImageUrl());
        newsES.setTag(newsESDetails.getTag());
        newsES.setPublishTime(newsESDetails.getPublishTime());
        newsES.setViews(newsESDetails.getViews());
        return newsESRepository.save(newsES);
    }

    public List<NewsES> getHottestOfWeek() {
        long now = System.currentTimeMillis() / 1000;
        return newsESRepository.findTopNByPublishTimeBetweenOrderByViewsDesc(now - 7 * 24 * 60 * 60, now);
    }

    /**
     * 增加浏览量
     * @param id
     */
    public void increaseViews(Long id) {
        viewsMap.compute(id, (k, v) -> v == null ? 1 : v + 1);
    }

    @Scheduled(fixedRate = 1000) // 1 seconds
    public void updateViews() {
        viewsMap.forEach((k, v) -> {
            Optional<NewsES> optionalNewsES = newsESRepository.findById(k);
            NewsES newsES = optionalNewsES.orElse(null);
            if (newsES != null) {
                newsES.setViews(newsES.getViews() + v);
                newsESRepository.save(newsES);
            }
        });
        viewsMap.clear();
    }

    /**
     * TODO: ID自增&阻塞队列定时上传新闻
     */
    @Scheduled(fixedRate = 1000) // 1 seconds
    public void processUploadNews() {
        uploadNews.forEach(newsES -> {
            newsESRepository.save(newsES);
        });
        uploadNews.clear();
    }

    public void addUploadNews(NewsES newsES) {
        uploadNews.add(newsES);
    }

}
