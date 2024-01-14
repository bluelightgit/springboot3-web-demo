package com.mySpring.demo.Repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mySpring.demo.Models.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    

    // 获取某用户的历史记录
    @Query(value = "SELECT * FROM visitor WHERE UUID = ?1", nativeQuery = true)
    List<Visitor> getHistoryByUUID(String UUID);

}
