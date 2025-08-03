package com.snowball.snowball.repository;

import com.snowball.snowball.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByUseYn(String useYn);

    Optional<Spot> findByIdAndUseYn(Long id, String useYn);

    List<Spot> findByOwnerIdAndScopeAndUseYn(Long ownerId, String scope, String useYn);

    List<Spot> findByUseYn(String useYn, Pageable pageable);

    @Query("SELECT s FROM Spot s WHERE s.useYn = 'Y' AND s.isPublic = true AND " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            " OR LOWER(s.category.name) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Spot> searchSpot(@Param("q") String q, Pageable pageable);

    // ⭐️ 인기 스팟 (게시글 많은 순)
    @Query("""
        SELECT s FROM Spot s
        LEFT JOIN s.posts p
        WHERE s.useYn = 'Y'
        GROUP BY s
        ORDER BY COUNT(p) DESC
    """)
    List<Spot> findPopularSpots(Pageable pageable);
    
    List<Spot> findByScopeAndUseYn(String scope, String useYn);
}