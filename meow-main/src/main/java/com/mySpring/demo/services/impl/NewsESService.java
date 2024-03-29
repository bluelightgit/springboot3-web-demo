package com.mySpring.demo.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mySpring.demo.services.INewsESService;
import com.mySpring.demo.models.news.pojos.NewsES;
import com.mySpring.demo.models.news.dtos.ViewUpdate;
import com.mySpring.demo.repositories.NewsESRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.mySpring.demo.constant.GlobalScheduledTime.TICK;
import static com.mySpring.demo.constant.KafkaTopic.VIEW_UPDATE_TOPIC;
import static com.mySpring.demo.constant.RedisPrefix.VIEWS_PREFIX;

@Service
public class NewsESService implements INewsESService {

    @Autowired
    private NewsESRepository newsESRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

//    private BlockingQueue<NewsES> uploadNews = new LinkedBlockingQueue<>();
//    private ConcurrentHashMap<Long, Integer> viewsMap = new ConcurrentHashMap<>();

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

    /**
     * 模糊搜索
     * @param title
     * @param content
     * @return
     */
    public List<NewsES> findByTitleOrContentWithPagination(String title, String content) {
        return newsESRepository.findByTitleOrContent(title, content);
    }

    public NewsES createNewsES(NewsES newsES) {
        return newsESRepository.save(newsES);
    }

    public void deleteNewsES(Long id) {
        newsESRepository.deleteById(id);
    }

    /**
     * 更新新闻
     * @param id
     * @param newsESDetails
     * @return
     */
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
        return newsESRepository.findByPublishTimeBetweenOrderByViewsDesc(now - 86400L * 7, now);
    }

    /**
     * 增加浏览量
     * @param id
     */
    public void increaseViews(Long id) throws JsonProcessingException {
        String views = stringRedisTemplate.opsForValue().get(VIEWS_PREFIX + id);
        if (views == null) {
            Optional<NewsES> optionalNewsES = newsESRepository.findById(id);
            NewsES newsES = optionalNewsES.orElse(null);
            if (newsES != null) {
                views = String.valueOf(newsES.getViews());
                stringRedisTemplate.opsForValue().set(VIEWS_PREFIX + id, views);
            }
        }
        stringRedisTemplate.opsForValue().increment(VIEWS_PREFIX + id);
//        sendViewUpdateMessage(new ViewUpdate(id, Long.parseLong(Objects.requireNonNull(views))));
    }

    @Scheduled(fixedRate = TICK) // 1 seconds
    public void updateViews() {
        Set<String> keys = stringRedisTemplate.keys(VIEWS_PREFIX + "*");
        if (keys != null) {
            keys.forEach(key -> {
                Long newsId = Long.parseLong(key.substring(VIEWS_PREFIX.length()));
                Optional<NewsES> optionalNewsES = newsESRepository.findById(newsId);
                NewsES newsES = optionalNewsES.orElse(null);
                String value = stringRedisTemplate.opsForValue().get(key);
                if (newsES != null && value != null) {
                    newsES.setViews(Long.parseLong(value));
                    newsESRepository.save(newsES);
                }
            });
        }
    }
    public Long getViews(Long id) {
        String views = stringRedisTemplate.opsForValue().get(VIEWS_PREFIX + id);
        if (views == null) {
            NewsES newsES = newsESRepository.findById(id).orElse(null);
            if (newsES != null && newsES.getViews() != null) {
                views = String.valueOf(newsES.getViews());
                stringRedisTemplate.opsForValue().set(VIEWS_PREFIX + id, views);
            }
        }
        return Long.parseLong(views);
    }

    private void sendViewUpdateMessage(ViewUpdate viewUpdate) throws JsonProcessingException {
        kafkaTemplate.send(VIEW_UPDATE_TOPIC, objectMapper.writeValueAsString(viewUpdate));
    }

    /**
     * ID自增&阻塞队列定时上传新闻
     */
//    @Scheduled(fixedRate = 1000) // 1 seconds
//    public void processUploadNews() {
//        if (uploadNews.isEmpty()) {
//            return;
//        }
//        Long maxId = getMaxId();
//        logger.info("current Max id: {}", maxId);
//        AtomicReference<Long> count = new AtomicReference<>(1L);
//        uploadNews.forEach(newsES -> {
//            newsES.setId(maxId + count.get());
//            newsESRepository.save(newsES);
//            count.updateAndGet(v -> v + 1L);
//        });
//        if (!uploadNews.isEmpty()) {
//            logger.info("Upload {} news", uploadNews.size());
//        }
//        uploadNews.clear();
//    }
//
//    public void addUploadNews(NewsES newsES) {
//        uploadNews.add(newsES);
//    }

//    @Scheduled(fixedRate = TICK) // 1 seconds
//    public void processUploadNews() throws JsonProcessingException {
//        String news = stringRedisTemplate.opsForList().leftPop("uploadNews");
//        if (news == null) {
//            return;
//        }
//        Long maxId = getMaxId();
//        logger.info("current Max id: {}", maxId);
//        long count = 1;
//        while (news != null) {
//            NewsES newsES = objectMapper.readValue(news, NewsES.class);
//            newsES.setId(maxId + count);
//            count++;
//            newsESRepository.save(newsES);
//            logger.info("Upload news: {}, Id: {}", newsES.getTitle(), newsES.getId());
//            news = stringRedisTemplate.opsForList().leftPop("uploadNews");
//        }
//        logger.info("Upload {} news", count - 1);
//    }
//
//    public void addUploadNews(NewsES newsES) throws JsonProcessingException {
//        stringRedisTemplate.opsForList().rightPush("uploadNews", objectMapper.writeValueAsString(newsES));
//    }

//    public Long getMaxId() {
//        List<NewsES> newsESList = newsESRepository.findByPublishTimeBetweenOrderByIdDesc(0L, System.currentTimeMillis() / 1000L + 60000L);
//        return newsESList.get(0).getId();
//    }
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
