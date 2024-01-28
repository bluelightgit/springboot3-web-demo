package com.mySpring.demo.Repositories;

import com.mySpring.demo.Models.NewsES;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NewsESRepository extends ElasticsearchRepository<NewsES, Long> {

    /**
     * 根据标题查询
     * @param title
     * @return
     */
    List<NewsES> findByTitle(String title);

    /**
     * 根据标题和内容查询
     * @param title
     * @param content
     * @return
     */
    List<NewsES> findByTitleOrContent(String title, String content);

    /**
     * 分页查询
     * @param title
     * @param content
     * @param pageable
     * @return
     */
//    List<NewsES> findByTitleOrContentWithPagination(String title, String content, Pageable pageable);


}