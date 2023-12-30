package com.mySpring.demo.Services;

import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Repositories.VisitorRepository;
import com.mySpring.demo.Models.VisitorHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public Visitor getVisitor(String deviceInfo) {
        Visitor visitor = visitorRepository.findByDeviceInfo(deviceInfo);
        if (visitor != null) {
            return visitor;
        } else {
            return null;
        }
    }

    public Visitor createVisitor(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    public Visitor updateVisitor(String deviceInfo, Visitor visitorDetails) {
        Visitor visitor = visitorRepository.findByDeviceInfo(deviceInfo);
        if (visitor != null) {
            visitor.setDeviceInfo(deviceInfo);
            visitor.setIpAddress(visitorDetails.getIpAddress());
            visitor.setDeviceType(visitorDetails.getDeviceType());
            visitor.setNewsId(visitorDetails.getNewsId());
            visitor.setTimeStamp(visitorDetails.getTimeStamp());
            return visitorRepository.save(visitor);
        } else {
            return null;
        }

    }

    public void deleteVisitor(String id) {
        visitorRepository.deleteByDeviceInfo(id);
    }

    public void addToHistory(Visitor visitor) {
        // List<VisitorHistory> 用户所有历史
        List<VisitorHistory> history = visitor.getHistory();
        // VisitorHistory 用户当前历史
        VisitorHistory newHistory = new VisitorHistory();
        newHistory.addTimeAndNews(visitor.getTimeStamp(), visitor.getNewsId());
        history.add(newHistory);
        visitor.setHistory(history);
        visitorRepository.save(visitor);
    }
    
}
