package com.mySpring.demo.recommendation.common;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Model {

    private Word2Vec vec;

    private TokenizerFactory t;

//    private SentenceIterator iter;

    public Model(TokenizerFactory tokenizerFactory) {
        this.t = tokenizerFactory;
//        this.iter = iterator;
    }

    public void train(List<String> texts) {
        SentenceIterator iter = new CollectionSentenceIterator(texts);
        this.t.setTokenPreProcessor(new CommonPreprocessor());
        this.vec = new Word2Vec.Builder()
                .minWordFrequency(2)
                .iterations(2)
                .layerSize(512)
                .seed(8)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(this.t)
                .build();
        vec.fit();
    }

    public static WordVectors load(String path) throws IOException {
        return WordVectorSerializer.loadTxtVectors(new File(path));
    }

    public Word2Vec getVec() {
        return this.vec;
    }


}
