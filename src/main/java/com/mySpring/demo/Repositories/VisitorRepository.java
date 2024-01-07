package com.mySpring.demo.Repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mySpring.demo.Models.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    
    @Query(value = "SELECT * FROM visitor WHERE UUID = ?1", nativeQuery = true)
    Visitor findByUUID(String UUID);

    @Query(value = "DELETE FROM visitor WHERE UUID = ?1", nativeQuery = true)
    void deleteByUUID(String UUID);

    // @Query(value = "SELECT * FROM visitor WHERE cookie = ?1", nativeQuery = true)
    // Visitor findByCookie(String cookie);

    // @Query(value = "DELETE FROM visitor WHERE cookie = ?1", nativeQuery = true)
    // void deleteByCookie(String cookie);

    // 获取某用户的历史记录
    @Query(value = "SELECT * FROM visitor WHERE UUID = ?1", nativeQuery = true)
    List<Visitor> getHistoryByUUID(String UUID);

    // 检测某UUID是否存在
    // @Query(value = "SELECT * FROM visitor WHERE UUID = ?1", nativeQuery = true)
    // Visitor checkUUID(String UUID);
}
