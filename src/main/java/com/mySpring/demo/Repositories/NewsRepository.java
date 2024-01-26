package com.mySpring.demo.Repositories;

import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT n FROM News n WHERE n.title = :title")
    List<News> findNewsByTitle(String title);

    // 获取所有重复的新闻
    @Query(value = """
            select * from news
            where id not in (
                select id from (
                    select min(id) as id from news
                    group by title
                ) as tmp
            );""", nativeQuery = true)
    List<News> findAllDuplicateNews();

    // views增加n
    @Modifying
    @Query(value = "UPDATE news SET views = views + ?2 WHERE id = ?1", nativeQuery = true)
    void updateViews(Long id, Integer n);

    @Query(value = "SELECT * FROM news ORDER BY publish_time DESC LIMIT 10", nativeQuery = true)
    List<News> findTop10ByOrderByPublishTimeDesc();

    // 某一时间段内观看数最高的news
    @Query(value = "SELECT * FROM news WHERE publish_time BETWEEN ?1 AND ?2 ORDER BY views DESC LIMIT ?3", nativeQuery = true)
    List<News> findTopNByPublishTimeBetweenOrderByViewsDesc(Long startTime, Long endTime, Integer n);
}
