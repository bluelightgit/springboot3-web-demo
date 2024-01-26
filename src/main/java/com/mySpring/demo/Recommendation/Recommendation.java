package com.mySpring.demo.Recommendation;

import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Repositories.NewsRepository;
import com.mySpring.demo.Repositories.VisitorRepository;
import com.mySpring.demo.Services.NewsService;
import com.mySpring.demo.Services.VisitorService;

import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.stopwords.StopWords;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

@Component
public class Recommendation {



    @Autowired
    VisitorService visitorService;

    @Autowired
    NewsService newsService;
    
    private static final Logger logger = LoggerFactory.getLogger(Recommendation.class);

    public List<News> getRecommendedNews(String uuid) throws IOException {

        logger.info("Generating recommendations for user: {}", uuid);

        List<Visitor> history = visitorService.getHistory(uuid);

        List<News> historyNews = new ArrayList<>();
        for (Visitor visitorHistory : history) {

            historyNews.add(newsService.getNews(visitorHistory.getNewsId()));
        }

        // 将历史新闻标题转换为字符串列表
        List<String> historyTitles = historyNews.stream().map(News::getTitle).toList();

        // 将历史新闻标题写入临时文件
        Path tempFile = Files.createTempFile("news_titles_"+uuid, ".txt");
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

        // WordVectors vec = WordVectorSerializer.loadTxtVectors(new File("src\\main\\resources\\static\\glove.6B.100d.txt"));

        // 获取所有新闻
        List<News> allNews = newsService.getAllNews();

        // 计算历史新闻与所有新闻的相似度
        Map<News, Double> similarityScores = new HashMap<>();

        Tokenizer tokenizer_0 = new DefaultTokenizerFactory().create(
                convertList2String(historyTitles)
                );
        List<String> historyKeyWords = tokenizer_0.getTokens();

        for (News news : allNews) {
            double similarity = 0;
            int count = 0;
            Tokenizer tokenizer = new DefaultTokenizerFactory().create(
                convertString2String(news.getTitle())
                );
            List<String> keyWords = tokenizer.getTokens();
            for (String historyKeyWord : historyKeyWords) {
                for (String keyWord : keyWords) {
                    double temp = vec.similarity(historyKeyWord, keyWord);
                    if (temp > 0.3) {
                        similarity += temp;
                        count += 1;
                    }
                    // System.out.println("history:"+ historyKeyWord + " keyWord: " + keyWord + "similarity: " + temp);
                }

            }
            similarityScores.put(news, similarity);
            // if (similarity > 0.1) {
            //     System.out.println(news.getTitle() + " : " + similarity);
            // }

        }

        // 根据相似度排序，选择最相似的新闻推荐给用户
        return similarityScores.entrySet().parallelStream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    private List<List<String>> getListKeyWords(List<String> ListString) {
        List<List<String>> allKeyWords = new ArrayList<>();
        for (String str : ListString) {
            Tokenizer tokenizer = new DefaultTokenizerFactory().create(str);
            List<String> keyWords = tokenizer.getTokens();
            allKeyWords.add(keyWords);
        }
        return allKeyWords;
    }

    // 去除字符串中的标点符号和数字
    private String convertList2String(List<String> list) {
        String str = list.toString();
        str = str.replaceAll("[\\pP\\p{Punct}]", " ");

        // 创建一个 StringBuilder 来构建最终的字符串
        StringBuilder sb = new StringBuilder();

        // 分割字符串并处理停用词
        for (String word : str.split(" ")) {
            if (!StopWords.getStopWords().contains(word)) {
                sb.append(word).append(" ");
            }
        }

        // 返回处理后的字符串
        return sb.toString().trim();
    }
    // 去除字符串中的标点符号和数字
    private String convertString2String(String str) {
        str = str.replaceAll("[\\pP\\p{Punct}]", " ");

        // 创建一个 StringBuilder 来构建最终的字符串
        StringBuilder sb = new StringBuilder();

        // 分割字符串并处理停用词
        for (String word : str.split(" ")) {
            if (!StopWords.getStopWords().contains(word)) {
                sb.append(word).append(" ");
            }
        }

        // 返回处理后的字符串
        return sb.toString().trim();
    }


}