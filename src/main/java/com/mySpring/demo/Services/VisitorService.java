package com.mySpring.demo.Services;

import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Repositories.VisitorRepository;
import com.mySpring.demo.Models.VisitorHistory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public Visitor getVisitor(Long deviceInfo) {
        Optional<Visitor> visitor =  visitorRepository.findById(deviceInfo);
        return visitor.orElse(null);
    }

    public List<Visitor> getVisitorByUUID(String UUID) {
        return visitorRepository.getHistoryByUUID(UUID);
    }

    public Visitor createVisitor(Visitor visitor) {

        return visitorRepository.save(visitor);
    }

    public Visitor updateVisitor(Long deviceInfo, Visitor visitorDetails) {
        Optional<Visitor> Optionalvisitor = visitorRepository.findById(deviceInfo);
        if (Optionalvisitor.isPresent()) {
            Visitor visitor = Optionalvisitor.get();
            visitor.setDeviceInfo(deviceInfo);
            visitor.setIpAddress(visitorDetails.getIpAddress());
            visitor.setDeviceType(visitorDetails.getDeviceType());
            visitor.setNewsId(visitorDetails.getNewsId());
            visitor.setTimeStamp(visitorDetails.getTimeStamp());
            visitor.setUUID(visitorDetails.getUUID());
            return visitorRepository.save(visitor);
        } else {
            return null;
        }

    }

    public void deleteVisitor(Long id) {
        visitorRepository.deleteById(id);
    }

    public boolean checkUUID(String UUID) {
        List<Visitor> visitor = visitorRepository.getHistoryByUUID(UUID);
        if (visitor == null) {
            return false;
        } else {
            return true;
        }
    }

    // public void addToHistory(Visitor visitor) {
    //     // List<VisitorHistory> 用户所有历史
    //     List<VisitorHistory> history = visitor.getHistory();
    //     // VisitorHistory 用户当前历史
    //     VisitorHistory newHistory = new VisitorHistory();
    //     newHistory.addTimeAndNews(visitor.getTimeStamp(), visitor.getNewsId());
    //     history.add(newHistory);
    //     visitor.setHistory(history);
    //     visitorRepository.save(visitor);
    // }
    
}
