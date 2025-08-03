package com.snowball.snowball.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;

import com.snowball.snowball.repository.SpotRepository;
import com.snowball.snowball.repository.UserRepository;
import com.snowball.snowball.entity.Spot;
import com.snowball.snowball.entity.User;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SpotRepository spotRepository;
    private final UserRepository userRepository;

    @Autowired
    public SearchController(SpotRepository spotRepository, UserRepository userRepository) {
        this.spotRepository = spotRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Map<String, Object> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        Map<String, Object> result = new HashMap<>();
        Pageable pageable = PageRequest.of(offset / limit, limit);

        if ("spot".equals(type) || "all".equals(type)) {
            List<Spot> spots = spotRepository.searchSpot(q, pageable);
            result.put("spots", spots);
        }
        if ("user".equals(type) || "all".equals(type)) {
            List<User> users = userRepository.searchUser(q, pageable);
            result.put("users", users);
        }
        // 추후: guide/search 등 확장
        return result;
    }

    // 최근 생성된 스팟
    @GetMapping("/recent")
    public List<Spot> getRecentSpots(@RequestParam(defaultValue = "8") int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return spotRepository.findByUseYn("Y", pageable);
    }

    // 인기 스팟 (spot_posts와 조인 필요)
    // /api/search/popular로만 접근 가능, 가변경로(/api/search/{id})와 절대 충돌 없음!
    @GetMapping("/popular")
    public List<Spot> getPopularSpots(@RequestParam(defaultValue = "8") int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return spotRepository.findPopularSpots(pageable); // 별도 JPQL/Native Query 필요
    }

}