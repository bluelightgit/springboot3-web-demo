package com.mySpring.demo.Services;

import com.mySpring.demo.Models.User;
import com.mySpring.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        // TODO: add registration logic
    }

    public User login(String username, String password) {
        // TODO: add login logic
    }

    public void logout(String username) {
        // TODO: add logout logic
    }
}
