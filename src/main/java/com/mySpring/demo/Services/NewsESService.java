package com.mySpring.demo.Services;


import com.mySpring.demo.Interfaces.INewsESService;
import com.mySpring.demo.Models.NewsES;
import com.mySpring.demo.Repositories.NewsESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsESService implements INewsESService {

    @Autowired
    private NewsESRepository newsESRepository;

    public NewsES getNewsES(Long id) {
        Optional<NewsES> newsES = newsESRepository.findById(id);
        return newsES.orElse(null);
    }

    public Iterable<NewsES> getAllNewsES() {
        return newsESRepository.findAll();
    }

    public List<NewsES> findByTitle(String title) {
        return newsESRepository.findByTitle(title);
    }

    public List<NewsES> findByTitleOrContent(String title, String content) {
        return newsESRepository.findByTitleOrContent(title, content);
    }

    public NewsES createNewsES(NewsES newsES) {
        return newsESRepository.save(newsES);
    }

    public void deleteNewsES(Long id) {
        newsESRepository.deleteById(id);
    }
    public NewsES updateNewsES(Long id, NewsES newsESDetails) {
        Optional<NewsES> optionalNewsES = newsESRepository.findById(id);
        NewsES newsES = optionalNewsES.orElse(null);
        if (newsES == null) {
            return null;
        }
        newsES.setTitle(newsESDetails.getTitle());
        newsES.setContent(newsESDetails.getContent());
        newsES.setUrl(newsESDetails.getUrl());
        newsES.setImageUrl(newsESDetails.getImageUrl());
        newsES.setTag(newsESDetails.getTag());
        newsES.setPublishTime(newsESDetails.getPublishTime());
        newsES.setViews(newsESDetails.getViews());
        return newsESRepository.save(newsES);
    }

}
