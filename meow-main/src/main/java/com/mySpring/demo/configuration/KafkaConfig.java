package com.mySpring.demo.configuration;

import com.mySpring.demo.models.news.dtos.ViewUpdate;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.views-update-topic}")
    private String viewsUpdateTopic;

    @Bean
    public RecordMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    public NewTopic viewsUpdateTopic() {
        return new NewTopic(viewsUpdateTopic, 1, (short) 1);
    }

//    @Bean
//    public ProducerFactory<String, ViewUpdate> producerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ViewUpdateSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
}
