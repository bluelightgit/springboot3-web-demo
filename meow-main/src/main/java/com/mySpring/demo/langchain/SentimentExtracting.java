package com.mySpring.demo.langchain;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.*;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SentimentExtracting {

//    @Autowired
//    private OpenAiChatModel chatLanguageModel;

    SentimentAnalyzer sentimentAnalyzer;

    final static Logger logger = LoggerFactory.getLogger(SentimentExtracting.class);

    public SentimentExtracting(ChatLanguageModel chatLanguageModel) {
        this.sentimentAnalyzer = AiServices.create(SentimentAnalyzer.class, chatLanguageModel);
    }

    private interface SentimentAnalyzer {
        @UserMessage("Does {{it}} have a discriminate or illegal sentiment?")
        boolean isInvalid(String text);
    }

    public boolean isInvalidSentiment(String text) {
        return sentimentAnalyzer.isInvalid(text);
    }
}

