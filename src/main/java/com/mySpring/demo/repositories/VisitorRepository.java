package com.mySpring.demo.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mySpring.demo.Models.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    /**
     * @param UUID
     * @return
     */
    @Query(value = "SELECT * FROM visitor WHERE UUID = ?1", nativeQuery = true)
    Optional<List<Visitor>> getDetailsByUUID(String UUID);

    /**
     * 根据UUID查询访问历史
     * @param UUID
     * @return
     */
    @Query(value = "SELECT news_id FROM visitor WHERE UUID = ?1", nativeQuery = true)
    Optional<List<Long>> getHistoryByUUID(String UUID);

    /**
     * 根据UUID查询访问历史(去重)
     * @param UUID
     * @return
     */
    @Query(value = "SELECT DISTINCT news_id FROM visitor WHERE UUID = ?1", nativeQuery = true)
    Optional<List<Long>> getHistoryByUUIDDistinct(String UUID);
}
