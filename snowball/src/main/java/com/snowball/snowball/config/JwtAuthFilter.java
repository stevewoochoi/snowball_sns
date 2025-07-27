// src/main/java/com/snowball/snowball/config/JwtAuthFilter.java
package com.snowball.snowball.config;

import com.snowball.snowball.entity.User;
import com.snowball.snowball.repository.UserRepository;
import com.snowball.snowball.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Long userId = jwtUtil.validateTokenAndGetUserId(token);
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    request.setAttribute("user", user); // 핵심!
                }
            } catch (Exception e) {
                // 유효하지 않은 토큰이면 무시 (필요시 401 반환 처리 가능)
            }
        }
        filterChain.doFilter(request, response);
    }
}