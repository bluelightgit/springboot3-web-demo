package com.mySpring.demo.Recommendation;

import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Repositories.NewsRepository;
import com.mySpring.demo.Repositories.VisitorRepository;
import com.mySpring.demo.Services.NewsService;
import com.mySpring.demo.Services.VisitorService;

import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

@Component
public class Recommendation {

    @Autowired
    VisitorService visitorService;

    @Autowired
    NewsRepository newsRepository;

    public List<News> getRecommendedNews(String uuid) throws IOException {
        // 获取用户的历史记录
//        String uuid = visitor.getUUID();
        List<Visitor> history = visitorService.getHistory(uuid);
        List<Long> newsIdList = new ArrayList<>();
        for (Visitor visitorHistory : history) {
            newsIdList.add(visitorHistory.getNewsId());
        }
        // 获取历史记录的详细信息
        List<News> historyNews = newsRepository.findAllByIds(newsIdList);

        // 将历史新闻标题转换为字符串列表
        List<String> historyTitles = historyNews.stream().map(News::getTitle).toList();
        // 将历史新闻标题写入临时文件
        Path tempFile = Files.createTempFile("news_titles", ".txt");
        Files.write(tempFile, historyTitles, StandardCharsets.UTF_8);
        // 使用Word2Vec模型训练历史新闻标题
        SentenceIterator iter = new BasicLineIterator(tempFile.toFile().getAbsolutePath());
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(1)
                .iterations(5)
                .layerSize(256)
                .seed(2)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();
        vec.fit();

        // 获取所有新闻
        List<News> allNews = newsRepository.findAll();

        // 计算历史新闻与所有新闻的相似度
        Map<News, Double> similarityScores = new HashMap<>();
        for (News news : allNews) {
            double similarity = vec.similarity(news.getTitle(), historyTitles.toString());
            similarityScores.put(news, similarity);
        }

        // 根据相似度排序，选择最相似的新闻推荐给用户
        List<News> recommendedNews = similarityScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendedNews;
    }
}