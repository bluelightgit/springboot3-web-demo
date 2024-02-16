package com.mySpring.demo.controllers;

import com.mySpring.demo.models.user.dtos.LoginRequest;
import com.mySpring.demo.models.user.pojos.User;
import com.mySpring.demo.models.user.pojos.UserHistory;
import com.mySpring.demo.services.impl.UserHistoryService;
import com.mySpring.demo.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import com.mySpring.demo.models.visitor.pojos.Visitor;
import com.mySpring.demo.services.impl.VisitorService;

import java.util.List;

@RestController
public class VisitorController {
    
    @Autowired
    private VisitorService visitorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserHistoryService userHistoryService;

    @PostMapping("/visitors")
    @Operation(summary = "Create a new visitor")
    public Visitor createVisitor(@RequestBody Visitor visitor) {
        return visitorService.createVisitor(visitor);
    }

    @GetMapping("/visitors")
    @Operation(summary = "Get all visitors")
    public Iterable<Visitor> getAllVisitors() {
        return visitorService.getAllVisitors();
    }

    @GetMapping("/visitors/{id}")
    @Operation(summary = "Get a visitor by id")
    public Visitor getVisitor(@RequestBody Long id) {
        return visitorService.getVisitor(id);
    }

    @GetMapping("/visitors/UUID/{UUID}")
    @Operation(summary = "check if a visitor is blank")
    public boolean isBlankVisitor(@PathVariable("UUID") String UUID) {
        return visitorService.isBlankVisitor(UUID);
    }

    @GetMapping("/visitor/{UUID}/history")
    @Operation(summary = "Get visitor history")
    public List<Long> getVisitorHistory(@PathVariable("UUID") String UUID) {
        return visitorService.getHistory(UUID, false);
    }

    @GetMapping("/visitor/{UUID}/distinctHistory")
    @Operation(summary = "Get visitor distinct history")
    public List<Long> getVisitorDistinctHistory(@PathVariable("UUID") String UUID) {
        return visitorService.getHistory(UUID, true);
    }

    @PostMapping("/User/login")
    @Operation(summary = "User login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/User/register")
    @Operation(summary = "User registration")
    public ResponseEntity<?> userRegister(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/User/profile")
    @Operation(summary = "Get user profile")
    public User userProfile(@RequestBody String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/User/updateProfile")
    @Operation(summary = "Update user profile")
    public User userUpdateProfile(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/User/logout")
    @Operation(summary = "User logout")
    public void userLogout(@RequestBody String username) {
        userService.logout(username);
    }

    @PostMapping("/User/delete")
    @Operation(summary = "Delete a user")
    public void userDelete(@RequestBody String username) {
        Long id = userService.getUserByUsername(username).getId();
        userService.deleteUser(id);
    }

    @PostMapping("/User/createHistory")
    @Operation(summary = "Create a user history")
    public void createUserHistory(@RequestBody Visitor visitor) {
        userHistoryService.createUserHistory(new UserHistory(userService.getUserByUUID(visitor.getUUID()), visitor.getNewsId()));
    }

    @GetMapping("/User/{UserID}/history")
    @Operation(summary = "Get user history")
    public List<Long> getUserHistory(@RequestBody Long UserID) {
        return userHistoryService.getUserHistoryByUserId(UserID, false);
    }

    @GetMapping("/User/{UserID}/distinctHistory")
    @Operation(summary = "Get user distinct history")
    public List<Long> getUserDistinctHistory(@RequestBody Long UserID) {
        return userHistoryService.getUserHistoryByUserId(UserID, true);
    }

}
