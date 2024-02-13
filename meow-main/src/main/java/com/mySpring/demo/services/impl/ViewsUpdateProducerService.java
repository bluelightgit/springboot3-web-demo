package com.mySpring.demo.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ViewsUpdateProducerService {

    private static final Logger logger = LoggerFactory.getLogger(ViewsUpdateProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public ViewsUpdateProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendViewsUpdateMessage(String topic, Object o) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("views-update-topic", o);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("Unable to send message=[{}] due to : {}", o, ex.getMessage());
            } else {
                logger.info("Sent message=[{}] with offset=[{}]", o, result.getRecordMetadata().offset());
            }
        });
    }
}
