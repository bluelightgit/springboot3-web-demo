package com.mySpring.demo.Models;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "news")
public class NewsES {

    @Id
    @Field(index = false, type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart", store = true)
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart", store = true)
    private String content;

    private String url;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String imageUrl;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String tag;

    @Field(type = FieldType.Long)
    private Long publishTime;

    @Field(type = FieldType.Long)
    private Long views;

    // getters and setters
    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public Long getViews() {
        return views;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public void setViews(Long views) {
        this.views = views;
    }


}