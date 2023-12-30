package com.mySpring.demo.Repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mySpring.demo.Models.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("SELECT n FROM News n WHERE n.id IN :ids")
    List<News> findAllByIds(@Param("ids") List<Long> ids);
    
}
