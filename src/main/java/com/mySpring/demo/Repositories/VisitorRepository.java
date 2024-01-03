package com.mySpring.demo.Repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mySpring.demo.Models.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    
    @Query(value = "SELECT * FROM visitor WHERE cookie = ?1", nativeQuery = true)
    Visitor findByDeviceInfo(String cookie);

    @Query(value = "DELETE FROM visitor WHERE cookie = ?1", nativeQuery = true)
    void deleteByDeviceInfo(String cookie);

}
