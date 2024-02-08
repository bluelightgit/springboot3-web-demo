package com.mySpring.demo.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mySpring.demo.models.news.dtos.ViewUpdate;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ViewUpdateSerializer implements Serializer<ViewUpdate> {

    @Override
    public byte[] serialize(String topic, ViewUpdate data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return retVal;
    }
}