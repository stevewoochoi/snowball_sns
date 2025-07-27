package com.snowball.snowball.repository;

import com.snowball.snowball.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {
}