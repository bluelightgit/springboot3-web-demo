package com.mySpring.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Services.VisitorService;

// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// @CrossOrigin
@RestController
public class VisitorController {
    
    @Autowired
    private VisitorService visitorService;

    // @CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.POST, RequestMethod.GET}, allowCredentials = "true")
    @PostMapping("/visitors")
    public Visitor createVisitor(@RequestBody Visitor visitor) {
        return visitorService.createVisitor(visitor);
    }

    @GetMapping("/visitors")
    public Iterable<Visitor> getAllVisitors() {
        return visitorService.getAllVisitors();
    }

    @GetMapping("/visitors/{deviceInfo}")
    public Visitor getVisitor(@RequestBody Long deviceInfo) {
        return visitorService.getVisitor(deviceInfo);
    }
}
