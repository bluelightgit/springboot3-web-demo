package com.mySpring.demo.Repositories;

import com.mySpring.demo.Models.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    @Query(value = "SELECT news_id FROM user_history WHERE user_id = ?1", nativeQuery = true)
    Optional<List<Long>> findByUserId(Long userId);

    // Distinct
    @Query(value = "SELECT DISTINCT news_id FROM user_history WHERE user_id = ?1", nativeQuery = true)
    Optional<List<Long>> findDistinctNewsIdByUserId(Long userId);

}
