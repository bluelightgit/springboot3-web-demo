package com.mySpring.demo.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mySpring.demo.Models.NewsES;
import com.mySpring.demo.Models.ViewUpdate;
import com.mySpring.demo.repositories.NewsESRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class ViewsSubscriber {

    @Autowired
    private NewsESRepository newsESRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @PostConstruct
    public void subscribe() {

        Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection().subscribe(
                (message, pattern) -> {
                    ViewUpdate viewUpdate = null;
                    try {
                        viewUpdate = objectMapper.readValue(message.getBody(), ViewUpdate.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    updateViews(viewUpdate);
                },
                "viewsChannel".getBytes()
        );
    }

    public void updateViews(ViewUpdate viewUpdate) {
        Optional<NewsES> optionalNewsES = newsESRepository.findById(viewUpdate.getId());
        NewsES newsES = optionalNewsES.orElse(null);
        if (newsES != null) {
            newsES.setViews(viewUpdate.getViews());
            newsESRepository.save(newsES);
        }
    }
}