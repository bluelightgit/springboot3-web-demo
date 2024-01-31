package com.mySpring.demo.Repositories;

import com.mySpring.demo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
