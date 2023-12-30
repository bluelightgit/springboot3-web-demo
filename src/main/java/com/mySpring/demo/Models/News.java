package com.mySpring.demo.Models;

import java.util.List;

// import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.ElementCollection;
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
    // @ElementCollection
    private String url;
    private List<String> tag;
    private String title;
    private Long publishTime;
    @Lob
    private byte[] image;
    private String content;

    
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

    public List<String> getTag() {
        return tag;
    }

    public byte[] getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public Long getPublishTime() {
        return publishTime;
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

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }


}
