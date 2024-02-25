package com.mySpring.demo.services.impl;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mySpring.demo.models.news.dtos.UserUploadedNews;
import com.mySpring.demo.models.news.pojos.NewsES;
import com.mySpring.demo.repositories.NewsESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class UserUploadNewsService {

    @Autowired
    private NewsESRepository newsESRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public AtomicLong maxId = new AtomicLong(-1);

    public void setMaxId() {
        if (this.maxId.get() == -1) {
            Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
            List<NewsES> allNewsES = newsESRepository.findAll(pageable).getContent();
            if (allNewsES.isEmpty()) {
                this.maxId.set(0);
                return;
            }
            this.maxId.set(allNewsES.get(0).getId());
        }
    }

    public void addToTempNews(UserUploadedNews userUploadedNews) throws JacksonException {
        long tempId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        userUploadedNews.setId(tempId);
        stringRedisTemplate.opsForList().rightPush(String.valueOf(tempId), objectMapper.writeValueAsString(userUploadedNews));
    }

    public List<UserUploadedNews> getTempNews() throws JacksonException {
        List<String> tempNews = stringRedisTemplate.opsForList().range("tempNews", 0, -1);
        List<UserUploadedNews> userUploadedNews = new ArrayList<>();
        if (tempNews != null) {
            for (String news : tempNews) {
                userUploadedNews.add(objectMapper.readValue(news, UserUploadedNews.class));
            }
        }
        return userUploadedNews;
    }

    public void submitTempNews(List<UserUploadedNews> tempNews) throws JacksonException {
        setMaxId();
        for (UserUploadedNews news : tempNews) {
            if (news.getStatus() == 1) {
                NewsES newsES = new NewsES();
                newsES.setId(maxId.incrementAndGet());
                newsES.setTitle(news.getTitle());
                newsES.setContent(news.getContent());
                newsES.setUrl(news.getUrl());
                newsES.setImageUrl(news.getImageUrl());
                newsES.setTag(news.getTag());
                newsES.setPublishTime(news.getPublishTime());
                newsES.setViews(news.getViews());
                newsESRepository.save(newsES);

                stringRedisTemplate.opsForList().remove("tempNews", 1, objectMapper.writeValueAsString(news));
            } else if (news.getStatus() == 2) {
                stringRedisTemplate.opsForList().remove("tempNews", 1, objectMapper.writeValueAsString(news));
            }

        }
    }

    /**
     * Change the status of the news and update to the redis
     * @param userUploadedNews
     * @param status
     */
    public void changeStatus(UserUploadedNews userUploadedNews, int status) throws JsonProcessingException {
        userUploadedNews.setStatus(status);
        stringRedisTemplate.opsForHash().put("tempNews", String.valueOf(userUploadedNews.getId()), objectMapper.writeValueAsString(userUploadedNews));
    }
}