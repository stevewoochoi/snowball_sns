package com.snowball.snowball.config.repository;

import com.snowball.snowball.entity.SpotBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotBoardRepository extends JpaRepository<SpotBoard, Long> {
    List<SpotBoard> findBySpot_Id(Long spotId); // ← 이걸로!
}