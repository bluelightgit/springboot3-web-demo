
// package com.mySpring.demo.Services;

// import com.mySpring.demo.Models.Visitor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.mySpring.demo.Models.News;
// import com.mySpring.demo.Repositories.NewsRepository;
// import com.mySpring.demo.Repositories.VisitorRepository;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class AdminService {

//     @Autowired
//     private NewsRepository newsRepository;

//     @Autowired
//     private VisitorRepository visitorRepository;

//     public List<News> getAllNews() {
//         return newsRepository.findAll();
//     }

//     public News getNews(Long id) {
//         Optional<News> news = newsRepository.findById(id);
//         return news.orElse(null);
//     }

//     public News createNews(News news) {
//         return newsRepository.save(news);
//     }

//     public News updateNews(Long id, News newsDetails) {
//         Optional<News> optionalNews = newsRepository.findById(id);
//         if(optionalNews.isPresent()) {
//             News news = optionalNews.get();
//             news.setTitle(newsDetails.getTitle());
//             news.setTag(newsDetails.getTag());
//             news.setImage(newsDetails.getImage());
//             news.setContent(newsDetails.getContent());
//             return newsRepository.save(news);
//         } else {
//             return null;
//         }
//     }

//     public void deleteNews(Long id) {
//         newsRepository.deleteById(id);
//     }

//     public List<Visitor> getAllVisitors() {
//         return visitorRepository.findAll();
//     }

//     public Visitor getVisitor(Long id) {
//         Optional<Visitor> Visitor = visitorRepository.findById(id);
//         return Visitor.orElse(null);
//     }

//     public Visitor createVisitor(Visitor Visitor) {
//         return visitorRepository.save(Visitor);
//     }

//     public Visitor updateVisitor(Long id, Visitor visitorDetails) {
//         Optional<Visitor> optionalVisitor = visitorRepository.findById(id);
//         if(optionalVisitor.isPresent()) {
//             Visitor visitor = optionalVisitor.get();
//             visitor.setDeviceInfo(visitorDetails.getDeviceInfo());
//             visitor.setIpAddress(visitorDetails.getIpAddress());
//             // visitor.setVisitTime(visitorDetails.gettimeStamp());
//             return visitorRepository.save(visitor);
//         } else {
//             return null;
//         }
//     }

//     public void deleteVisitor(Long id) {
//         visitorRepository.deleteById(id);
//     }
// }
