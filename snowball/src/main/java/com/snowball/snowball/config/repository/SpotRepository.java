package com.snowball.snowball.config.repository;

import com.snowball.snowball.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    // 기본 CRUD, findByCategory 등 확장 가능
}