// src/main/java/com/snowball/snowball/controller/UserController.java
package com.snowball.snowball.controller;

import com.snowball.snowball.entity.User;
import com.snowball.snowball.config.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User registerUser(@RequestBody User user) {
        user.setJoinedAt(LocalDateTime.now());
        user.setSnsType("EMAIL"); // 임시 로그인은 EMAIL
        user.setSnsId(user.getEmail());
        return userRepository.save(user);
    }
}