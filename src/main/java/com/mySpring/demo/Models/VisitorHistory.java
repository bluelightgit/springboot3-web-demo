package com.mySpring.demo.Models;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "visitor_history")
public class VisitorHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long newsId;

    private Long timeStamp;

    // @ElementCollection
    // private List<Long> newsIds;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    public void addTimeAndNews(Long timestamp, Long newsId) {
        this.timeStamp = timestamp;
        this.newsId = newsId;
    }
}