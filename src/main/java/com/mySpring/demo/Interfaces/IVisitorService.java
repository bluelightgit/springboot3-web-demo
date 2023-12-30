package com.mySpring.demo.Interfaces;
import  java.util.List;

import com.mySpring.demo.Models.News;
import com.mySpring.demo.Models.Visitor;

public interface IVisitorService {
    List<Visitor> getAllVisitors();
    Visitor getVisitor(String id);
    Visitor createVisitor(Visitor visitor);
    Visitor updateVisitor(String id, Visitor visitorDetails);
    void deleteVisitor(String id);
    void addToHistory(News news);
}
