// package com.mySpring.demo.Controllers;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.mySpring.demo.Services.NewsService;
// import com.mySpring.demo.Services.VisitorService;

// import java.util.List;

// import com.mySpring.demo.Models.Visitor;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PathVariable;

// @RestController
// @RequestMapping("/admin")
// public class AdminController {
    
//     @Autowired
//     private VisitorService visitorService;

//     @Autowired
//     private NewsService newsService;

//     @GetMapping("/visitors")
//     public List<Visitor> getAllVisitors() {
//         return visitorService.getAllVisitors();
//     }

//     @GetMapping("/visitors/{ipAddress}")
//     public ResponseEntity<Visitor> getVisitor(@PathVariable(value = "ipAddress") String deviceInfo) {
//         Visitor visitor = visitorService.getVisitor(deviceInfo);
//         return ResponseEntity.ok().body(visitor);
//     }

    
// }
