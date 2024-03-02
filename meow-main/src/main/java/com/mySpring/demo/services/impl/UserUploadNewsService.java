package com.mySpring.demo.services.impl;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mySpring.demo.langchain.SentimentExtracting;
import com.mySpring.demo.models.news.dtos.UserUploadedNews;
import com.mySpring.demo.models.news.pojos.NewsES;
import com.mySpring.demo.models.response.StatusResponse;
import com.mySpring.demo.repositories.NewsESRepository;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static com.mySpring.demo.constant.RedisPrefix.TEMP_NEWS_PREFIX;


@Service
public class UserUploadNewsService {

    @Autowired
    private NewsESRepository newsESRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private OpenAiChatModel chatLanguageModel;

    private final static Logger loger = LoggerFactory.getLogger(UserUploadNewsService.class);

    public AtomicLong maxId = new AtomicLong(-1);

//    private List<UserUploadedNews> tempNews = new ArrayList<>();

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
        stringRedisTemplate.opsForValue().set(TEMP_NEWS_PREFIX + tempId, objectMapper.writeValueAsString(userUploadedNews));
    }

    public List<UserUploadedNews> getAllTempNews() throws JacksonException {
        Set<String> keys = stringRedisTemplate.keys(TEMP_NEWS_PREFIX + "*");
        List<UserUploadedNews> userUploadedNews = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {
                String news = stringRedisTemplate.opsForValue().get(key);
                if (news != null) {
                    userUploadedNews.add(objectMapper.readValue(news, UserUploadedNews.class));
                }
            }
        }
//        this.tempNews = userUploadedNews;
        return userUploadedNews;
    }

    public UserUploadedNews getTempNewsById(Long id) throws JacksonException {
        String news = stringRedisTemplate.opsForValue().get(TEMP_NEWS_PREFIX + id);
        if (news != null) {
            return objectMapper.readValue(news, UserUploadedNews.class);
        }
        return null;
    }

    public ResponseEntity<?> submitTempNews() throws JacksonException {
        setMaxId();
        int count = 0;
        StatusResponse statusResponse = new StatusResponse();
        try {
            List<UserUploadedNews> tempNews = getAllTempNews();
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
                    stringRedisTemplate.delete(TEMP_NEWS_PREFIX + news.getId());
                    count++;
                } else if (news.getStatus() == 2) {
                    stringRedisTemplate.delete(TEMP_NEWS_PREFIX + news.getId());
                }
            }
            statusResponse.setStatus(HttpStatus.OK.value());
            statusResponse.setMessage("Submit success");
            loger.info("Submit {} news", count);
            loger.info("current max id: {}", maxId.get());
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            statusResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(statusResponse, HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Change the status of the news and update to the redis, status 0: pending, 1: approved, 2: rejected
     * @param userUploadedNews
     * @param status
     * @return
     * @throws JsonProcessingException
     */
    public ResponseEntity<?> changeStatus(UserUploadedNews userUploadedNews, int status) throws JsonProcessingException {
        userUploadedNews.setStatus(status);
        StatusResponse statusResponse = new StatusResponse();
        try {
            stringRedisTemplate.opsForValue().set(TEMP_NEWS_PREFIX + userUploadedNews.getId(), objectMapper.writeValueAsString(userUploadedNews));
            statusResponse.setStatus(HttpStatus.OK.value());
            statusResponse.setMessage("Update status success");
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            statusResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(statusResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Review the news by AI (sentiment analysis)
     * @param userUploadedNews
     * @return
     * @throws JsonProcessingException
     */
    public ResponseEntity<?> reviewByAI(UserUploadedNews userUploadedNews) throws JsonProcessingException {
        StatusResponse statusResponse = new StatusResponse();
        try {
            SentimentExtracting sentimentExtracting = new SentimentExtracting(chatLanguageModel);
            boolean isInvalidNewsTitle = sentimentExtracting.isInvalidSentiment(userUploadedNews.getTitle());
            boolean isInvalidNewsContent = sentimentExtracting.isInvalidSentiment(userUploadedNews.getContent());
            if (isInvalidNewsTitle || isInvalidNewsContent) {
                userUploadedNews.setStatus(2); // rejected
            } else {
                userUploadedNews.setStatus(1); // approved
            }
        } catch (Exception e) {
            statusResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            statusResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(statusResponse, HttpStatus.BAD_REQUEST);
        }
        try {
            stringRedisTemplate.opsForValue().set(TEMP_NEWS_PREFIX + userUploadedNews.getId(), objectMapper.writeValueAsString(userUploadedNews));
            statusResponse.setStatus(HttpStatus.OK.value());
            statusResponse.setMessage("Update status success");
            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            statusResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            statusResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(statusResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
