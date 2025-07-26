package com.snowball.snowball.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.snowball.snowball.entity.SpotBoardPost;

@Repository
public interface SpotBoardPostRepository extends JpaRepository<SpotBoardPost, Long> {
    List<SpotBoardPost> findByBoardIdOrderByCreatedAtDesc(Long boardId);
    // 또는 spotId로 조회하는 경우
    List<SpotBoardPost> findByBoard_Spot_IdOrderByCreatedAtDesc(Long spotId);
}