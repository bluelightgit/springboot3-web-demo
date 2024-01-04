package com.mySpring.demo.Utils;

import java.util.List;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import com.mySpring.demo.Models.News;

/*
 * 推荐系统DL4J相关
 */
public class Recommendation {
    public List<String> getRecommendation() {
        // 数据预处理
        SentenceIterator iter = new BasicLineIterator(filePath);
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        // 训练词向量
        Word2Vec vec = new Word2Vec.Builder()
            .minWordFrequency(5)
            .iterations(1)
            .layerSize(100)
            .seed(42)
            .windowSize(5)
            .iterate(iter)
            .tokenizerFactory(t)
            .build();
        vec.fit();

        // 训练文本分类模型
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .updater(new Adam())
            .l2(1e-5)
            .list()
            .layer(new DenseLayer.Builder().nIn(100).nOut(50).activation(Activation.RELU).build())
            .layer(new OutputLayer.Builder().nIn(50).nOut(numClasses).activation(Activation.SOFTMAX).lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).build())
            .build();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        // 使用模型进行分类和推荐
        INDArray newsVector = vec.getWordVectorMatrix(news.getContent());
        int category = model.predict(newsVector);
        List<News> recommendedNews = newsRepository.findTop10ByCategoryOrderByViewsDesc(category);
    }
}
