package com.mySpring.demo.Models;

import java.util.HashMap;
import java.util.List;

import com.mySpring.demo.Utils.TextFormatDetector;

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
    private Long deviceInfo; // equals to id
    private String ipAddress;
    private String deviceType; // mobile, desktop
    private Long NewsId;
    private Long timeStamp;
    // private HashMap<Long, Long> history; // key: timestamp, value: newsId
    private String UUID;
    private String cookie;
    @ElementCollection
    private List<VisitorHistory> history;

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public Long getDeviceInfo() {
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

    public String getUUID() {
        return UUID;
    }

    public String getCookie() {
        return cookie;
    }

    public void setIpAddress(String ipAddress) {
        if (ipAddress == null || !TextFormatDetector.isIpAddress(ipAddress)) {
            this.ipAddress = "";
        } else {
            this.ipAddress = ipAddress;
        }
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceInfo(Long deviceInfo) {
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

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
