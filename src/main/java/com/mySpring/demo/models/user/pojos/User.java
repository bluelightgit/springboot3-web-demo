package com.mySpring.demo.models.user.pojos;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String UUID;

    // getters and setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = Objects.requireNonNullElse(password, "");
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNullElse(email, "");
    }

    public void setUUID(String UUID) {
        this.UUID = Objects.requireNonNullElse(UUID, "");
    }
}
