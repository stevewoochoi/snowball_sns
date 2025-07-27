// src/main/java/com/snowball/snowball/controller/UserController.java
package com.snowball.snowball.controller;

import com.snowball.snowball.entity.User;
import com.snowball.snowball.repository.UserRepository;
import com.snowball.snowball.util.JwtUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Autowired
    public UserController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public Map<String, Object> registerUser(@RequestBody User user) {
        user.setJoinedAt(LocalDateTime.now());
        user.setSnsType("EMAIL"); // 임시 로그인은 EMAIL
        user.setSnsId(user.getEmail());
        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getId());
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", saved.getId());
        resp.put("nickname", saved.getNickname());
        resp.put("token", token);
        return resp;
    }
}