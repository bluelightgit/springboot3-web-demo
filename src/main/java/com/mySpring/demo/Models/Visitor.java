package com.mySpring.demo.Models;

import com.mySpring.demo.Utils.TextFormatDetector;

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
    private Long id; // equals to id
    private String ipAddress;
    private String deviceType; // mobile, desktop
    private Long NewsId;
    private Long timeStamp;
    private String UUID;


    public String getIpAddress() {
        return ipAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public Long getId() {
        return id;
    }

    public Long getNewsId() {
        return NewsId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }


    public String getUUID() {
        return UUID;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setNewsId(Long NewsId) {
        this.NewsId = NewsId;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }


    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

}
