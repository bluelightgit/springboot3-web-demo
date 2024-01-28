package com.mySpring.demo.Models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Long publishTime;
    private Long views;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String url;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String imageUrl;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String tag;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    // @Lob
    // private byte[] image;


    
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
