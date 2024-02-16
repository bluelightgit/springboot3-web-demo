package com.mySpring.demo.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mySpring.demo.models.news.dtos.ViewUpdate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import com.mySpring.demo.repositories.NewsESRepository;
@Service
public class ViewsUpdateConsumerService {

    @Value("${kafka.topic.views-update-topic}")
    private String viewsUpdateTopic;

    private final Logger logger = LoggerFactory.getLogger(ViewsUpdateConsumerService.class);

    @Autowired
    private NewsESRepository newsESRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic.views-update-topic}", groupId = "views-update-consumer")
    public void consumeViewsUpdate(ConsumerRecord<String, String> consumerRecord) {
        try {
            ViewUpdate viewUpdate = objectMapper.readValue(consumerRecord.value(), ViewUpdate.class);
            logger.info("Consumed views update: {}", viewUpdate.getId());
            newsESRepository.findById(viewUpdate.getId()).ifPresent(newsES -> {
                newsES.setViews(viewUpdate.getViews() + newsES.getViews());
                newsESRepository.save(newsES);
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
