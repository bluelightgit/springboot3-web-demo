package com.mySpring.demo.recommendation;

import com.mySpring.demo.Models.NewsES;

import com.mySpring.demo.services.impl.NewsESService;
import com.mySpring.demo.services.impl.UserService;
import com.mySpring.demo.services.impl.VisitorService;

import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.stopwords.StopWords;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

@Component
public class Recommendation {

    @Autowired
    VisitorService visitorService;

    @Autowired
    UserService userService;

    @Autowired
    NewsESService newsESService;
    private static final Logger logger = LoggerFactory.getLogger(Recommendation.class);
//    private static final WordVectors vec;
//
//    static {
//        try {
//            vec = WordVectorSerializer.loadTxtVectors(new File("src\\main\\resources\\static\\glove.6B.100d.txt"));
//        } catch (IOException e) {
//            logger.error("Failed to load word2vec model");
//            throw new RuntimeException(e);
//        }
//    }
//    public recommendation() throws IOException {
//    }

    public List<NewsES> getRecommendedNews(String uuid, boolean isRegisteredUser) throws IOException {

        logger.info("Generating recommendations for user: {}", uuid);
        List<Long> historyList;
        if (isRegisteredUser) {
            historyList = userService.getHistory(userService.getUserByUUID(uuid).getId(), true);
        } else {
            historyList = visitorService.getHistory(uuid, true);
        }

        List<NewsES> historyNews = new ArrayList<>();
        for (Long history : historyList) {

            historyNews.add(newsESService.getNewsES(history));
        }

        // 将历史新闻标题转换为字符串列表
        List<String> historyTitles = historyNews.stream().map(NewsES::getTitle).toList();

//        // 将历史新闻标题写入临时文件
//        Path tempFile = Files.createTempFile("news_titles_"+uuid, ".txt");
//        Files.write(tempFile, historyTitles, StandardCharsets.UTF_8);
//        // 使用Word2Vec模型训练历史新闻标题
//        SentenceIterator iter = new BasicLineIterator(tempFile.toFile().getAbsolutePath());

        // 创建一个句子迭代器
        SentenceIterator iter = new CollectionSentenceIterator(historyTitles);
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
        Iterable<NewsES> allNews = newsESService.getAllNewsES();

        // 过滤掉历史新闻
        allNews = StreamSupport.stream(allNews.spliterator(), false)
                .filter(news -> !historyList.contains(news.getId()))
                .collect(Collectors.toList());

        // 计算历史新闻与所有新闻的相似度
        Map<NewsES, Double> similarityScores = new HashMap<>();

        Tokenizer tokenizer_0 = new DefaultTokenizerFactory().create(
                convertList2String(historyTitles)
                );
        List<String> historyKeyWords = tokenizer_0.getTokens();

        for (NewsES news : allNews) {
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