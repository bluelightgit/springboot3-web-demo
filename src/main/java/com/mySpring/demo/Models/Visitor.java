package com.mySpring.demo.Models;

import com.mySpring.demo.utils.TextFormatDetector;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
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


}
