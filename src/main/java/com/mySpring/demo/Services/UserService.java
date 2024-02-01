package com.mySpring.demo.Services;

import com.mySpring.demo.Interfaces.IUserService;
import com.mySpring.demo.Models.User;
import com.mySpring.demo.Models.UserHistory;
import com.mySpring.demo.Repositories.UserHistoryRepository;
import com.mySpring.demo.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getUser(Long id) {
        Optional<User> user =  userRepository.findById(id);
        return user.orElse(null);
    }

    public User getUserByUsername(String username) {
        Optional<User> user =  userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public User getUserByEmail(String email) {
        Optional<User> user =  userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public User getUserByUUID(String UUID) {
        Optional<User> user =  userRepository.findByUUID(UUID);
        return user.orElse(null);
    }


    public User register(User user) {
        if (getUserByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        } else if (getUserByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        } else if (getUserByUUID(user.getUUID()) != null) {
            throw new RuntimeException("UUID already exists");
        } else if (user.getPassword().length() < 6) {
            throw new RuntimeException("At least 6 characters for password");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            logger.info("New user registered: {}, id: {}", user.getUsername(), user.getId());
            return userRepository.save(user);
        }
    }

    public User login(User user) {
        User userDetails = getUserByUsername(user.getUsername());
        if (userDetails == null){
            throw new RuntimeException("User does not exist");
        } else if (!passwordEncoder.matches(userDetails.getPassword(),passwordEncoder.encode(user.getPassword()))) {
            throw new RuntimeException("Password is incorrect");
        } else {
            logger.info("User logged in: {}, id: {}", user.getUsername(), user.getId());
            return userDetails;
        }
    }

    public void logout(String username) {
        // TODO: add logout logic
        logger.info("User logged out: {}", username);
    }

    public User updateUser(User user) {
        String oldUsername = getUser(user.getId()).getUsername();
        String oldEmail = getUser(user.getId()).getEmail();
        if (getUserByUsername(user.getUsername()) != null && !Objects.equals(user.getUsername(), oldUsername)){
            throw new RuntimeException("Username already exists");
        } else if (getUserByEmail(user.getEmail()) != null && !Objects.equals(user.getEmail(), oldEmail)){
            throw new RuntimeException("Email already exists");
        } else if (passwordEncoder.matches(passwordEncoder.encode(user.getPassword()), getUser(user.getId()).getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old one");
        } else {
            logger.info("User updated: {}, id: {}", user.getUsername(), user.getId());
            return userRepository.save(user);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        logger.info("User deleted, id: {}", id);
    }

    public List<Long> getHistory(Long id, boolean isDistinct) {
        if (isDistinct) {
            return userHistoryRepository.findDistinctNewsIdByUserId(id).orElse(null);
        } else {
            return userHistoryRepository.findByUserId(id).orElse(null);
        }
    }
}
