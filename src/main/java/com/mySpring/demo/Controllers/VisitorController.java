package com.mySpring.demo.Controllers;

import com.mySpring.demo.Models.User;
import com.mySpring.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mySpring.demo.Models.Visitor;
import com.mySpring.demo.Services.VisitorService;

@RestController
public class VisitorController {
    
    @Autowired
    private VisitorService visitorService;

    @Autowired
    private UserService userService;

    @PostMapping("/visitors")
    public Visitor createVisitor(@RequestBody Visitor visitor) {
        return visitorService.createVisitor(visitor);
    }

    @GetMapping("/visitors")
    public Iterable<Visitor> getAllVisitors() {
        return visitorService.getAllVisitors();
    }

    @GetMapping("/visitors/{id}")
    public Visitor getVisitor(@RequestBody Long id) {
        return visitorService.getVisitor(id);
    }

    @PostMapping("/User/login")
    public User userLogin(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/User/register")
    public User userRegister(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/User/profile")
    public User userProfile(@RequestBody String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/User/updateProfile")
    public User userUpdateProfile(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/User/logout")
    public void userLogout(@RequestBody String username) {
        userService.logout(username);
    }

    @PostMapping("/User/delete")
    public void userDelete(@RequestBody String username) {
        Long id = userService.getUserByUsername(username).getId();
        userService.deleteUser(id);
    }

}
