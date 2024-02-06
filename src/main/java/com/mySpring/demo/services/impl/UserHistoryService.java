package com.mySpring.demo.services.impl;

import com.mySpring.demo.Models.UserHistory;
import com.mySpring.demo.repositories.UserHistoryRepository;
import com.mySpring.demo.services.IUserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHistoryService implements IUserHistoryService {

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    public void createUserHistory(UserHistory userHistory) {
        userHistoryRepository.save(userHistory);
    }

    public List<Long> getUserHistoryByUserId(Long userId, Boolean isDistinct) {
        if (isDistinct) {
            return userHistoryRepository.findDistinctNewsIdByUserId(userId).orElse(null);
        } else {
            return userHistoryRepository.findByUserId(userId).orElse(null);
        }
    }
}
