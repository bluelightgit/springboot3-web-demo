package com.mySpring.demo.Models;

import java.util.HashMap;
import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "visitor")
public class Visitor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String deviceInfo; // equals to id
    private String ipAddress;
    private String deviceType; // mobile, desktop
    private Long NewsId;
    private Long timeStamp;
    // private HashMap<Long, Long> history; // key: timestamp, value: newsId
    @ElementCollection
    private List<VisitorHistory> history;

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public Long getNewsId() {
        return NewsId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public List<VisitorHistory> getHistory() {
        return history;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setNewsId(Long NewsId) {
        this.NewsId = NewsId;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setHistory(List<VisitorHistory> history) {
        this.history = history;
    }

}
