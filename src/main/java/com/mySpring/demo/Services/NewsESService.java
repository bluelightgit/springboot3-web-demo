package com.mySpring.demo.Services;

import com.mySpring.demo.Interfaces.INewsESService;
import com.mySpring.demo.Models.NewsES;
import com.mySpring.demo.Repositories.NewsESRepository;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class NewsESService implements INewsESService {

    /**
     * TODO: use redis to replace ConcurrentHashMap and BlockingQueue
     */
    @Autowired
    private NewsESRepository newsESRepository;

    private BlockingQueue<NewsES> uploadNews = new LinkedBlockingQueue<>();
    private ConcurrentHashMap<Long, Integer> viewsMap = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(NewsESService.class);

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
        long now = System.currentTimeMillis() / 1000L;
        return newsESRepository.findByPublishTimeBetweenOrderByViewsDesc(now - 86400L, now);
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
     * ID自增&阻塞队列定时上传新闻
     */
    @Scheduled(fixedRate = 1000) // 1 seconds
    public void processUploadNews() {
        if (uploadNews.isEmpty()) {
            return;
        }
        Long maxId = getMaxId();
        logger.info("current Max id: {}", maxId);
        AtomicReference<Long> count = new AtomicReference<>(1L);
        uploadNews.forEach(newsES -> {
            newsES.setId(maxId + count.get());
            newsESRepository.save(newsES);
            count.updateAndGet(v -> v + 1L);
        });
        if (!uploadNews.isEmpty()) {
            logger.info("Upload {} news", uploadNews.size());
        }
        uploadNews.clear();
    }

    public void addUploadNews(NewsES newsES) {
        uploadNews.add(newsES);
    }

    public Long getMaxId() {

        List<NewsES> newsESList = newsESRepository.findByPublishTimeBetweenOrderByIdDesc(0L, System.currentTimeMillis() / 1000L);
        return newsESList.get(0).getId();
    }
//    public Long getMaxId() {
//
//        SearchRequest searchRequest = new SearchRequest("news");
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("max_id").field("id");
//
//        searchSourceBuilder.aggregation(maxAggregationBuilder);
//
//        searchRequest.source(searchSourceBuilder);
//
//        Long maxId = null;
//        try {
//            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//            Aggregations aggregations = response.getAggregations();
//            ParsedMax parsedMax = aggregations.get("max_id");
//            maxId = (long) parsedMax.value();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return maxId;
//    }

}
