package com.mySpring.demo.services.impl;

import com.mySpring.demo.models.visitor.pojos.Visitor;
import com.mySpring.demo.repositories.VisitorRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    @Cacheable(value = "visitor", key = "#id")
    public Visitor getVisitor(Long id) {
        Optional<Visitor> visitor =  visitorRepository.findById(id);
        return visitor.orElse(null);
    }

    public List<Visitor> getVisitorByUUID(String UUID) {
        return visitorRepository.getDetailsByUUID(UUID).orElse(null);
    }

    public Visitor createVisitor(Visitor visitor) {

        return visitorRepository.save(visitor);
    }

    public Visitor updateVisitor(Long id, Visitor visitorDetails) {
        Optional<Visitor> Optionalvisitor = visitorRepository.findById(id);
        if (Optionalvisitor.isPresent()) {
            Visitor visitor = Optionalvisitor.get();
            visitor.setId(id);
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

    public boolean isBlankVisitor(String UUID) {
        List<Long> visitor = visitorRepository.getHistoryByUUID(UUID).orElse(null);
        return visitor == null;
    }

    @Cacheable(value = "visitorHistory", key = "#UUID")
    public List<Long> getHistory(String UUID, boolean isDistinct) {
        if (isDistinct) {
            return visitorRepository.getHistoryByUUIDDistinct(UUID).orElse(null);
        } else {
            return visitorRepository.getHistoryByUUID(UUID).orElse(null);
        }
    }
    
}
