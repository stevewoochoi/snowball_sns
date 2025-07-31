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
        System.out.println("[JwtAuthFilter] Authorization 헤더: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("[JwtAuthFilter] 추출된 토큰: " + token);

            try {
                Long userId = jwtUtil.validateTokenAndGetUserId(token);
                System.out.println("[JwtAuthFilter] 토큰에서 추출된 userId: " + userId);
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    System.out.println("[JwtAuthFilter] user 객체 설정: id=" + user.getId() + ", nickname=" + user.getNickname());
                    request.setAttribute("user", user); // 핵심!
                } else {
                    System.out.println("[JwtAuthFilter] userRepository에 해당 userId가 존재하지 않습니다.");
                }
            } catch (Exception e) {
                System.out.println("[JwtAuthFilter] 토큰 검증 실패: " + e.getMessage());
                // 필요시 아래 활성화해서 401 반환
                // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                // return;
            }
        } else {
            System.out.println("[JwtAuthFilter] Authorization 헤더가 없음 또는 Bearer 타입이 아님.");
        }
        filterChain.doFilter(request, response);
    }
}