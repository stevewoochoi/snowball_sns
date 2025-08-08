package com.snowball.snowball.repository;

import com.snowball.snowball.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    // 1. 내 맵: ownerId, useYn (모두 노출)
    List<Spot> findByOwnerIdAndUseYn(Long ownerId, String useYn);

    // 2. 특정 ID + useYn (단건)
    Optional<Spot> findByIdAndUseYn(Long id, String useYn);

    // 3. 공개 범위 + useYn (scope로 필터링)
    List<Spot> findByScopeAndUseYn(String scope, String useYn);

    // 4. 전체 useYn=Y (모든 활성 스팟)
    List<Spot> findByUseYn(String useYn);

    // 5. 전체 useYn=Y + 페이징 (최근 스팟, 인기 스팟 등)
    List<Spot> findByUseYn(String useYn, org.springframework.data.domain.Pageable pageable);

    // 6. 남의 맵: PUBLIC, OFFICIAL, (FRIENDS + 친구만) 노출
    @Query("""
                SELECT s FROM Spot s
                WHERE s.ownerId = :ownerId
                  AND s.useYn = 'Y'
                  AND (
                    s.scope = 'PUBLIC'
                    OR s.scope = 'OFFICIAL'
                    OR (
                        s.scope = 'FRIENDS'
                        AND EXISTS (
                            SELECT 1 FROM UserFriend uf
                            WHERE uf.user.id = :ownerId
                              AND uf.friend.id = :viewerId
                              AND uf.status = 'ACCEPTED'
                        )
                    )
                  )
            """)
    List<Spot> findVisibleSpotsForViewer(
            @Param("ownerId") Long ownerId,
            @Param("viewerId") Long viewerId);

    // ⭐️ 인기 스팟 (게시글 많은 순)
    @Query("""
                SELECT s FROM Spot s
                LEFT JOIN s.posts p
                WHERE s.useYn = 'Y'
                GROUP BY s
                ORDER BY COUNT(p) DESC
            """)
    List<Spot> findPopularSpots(Pageable pageable);

    @Query("SELECT s FROM Spot s WHERE s.useYn = 'Y' AND " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            " OR LOWER(s.category.name) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Spot> searchSpot(@Param("q") String q, Pageable pageable);

    @Query("""
                SELECT s FROM Spot s
                WHERE s.useYn = 'Y'
                  AND (
                    s.ownerId = :viewerId
                    OR s.scope = 'PUBLIC'
                    OR s.scope = 'OFFICIAL'
                    OR (
                      s.scope = 'FRIENDS'
                      AND EXISTS (
                        SELECT 1 FROM UserFriend uf
                        WHERE uf.user.id = s.ownerId
                          AND uf.friend.id = :viewerId
                          AND uf.status = 'ACCEPTED'
                      )
                    )
                  )
                  AND (
                    LOWER(s.name) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(s.category.name) LIKE LOWER(CONCAT('%', :q, '%'))
                  )
            """)
    List<Spot> searchSpotVisibleToUser(
            @Param("q") String q,
            @Param("viewerId") Long viewerId,
            Pageable pageable);

    @Query("""
                SELECT s FROM Spot s
                WHERE s.useYn = 'Y'
                  AND (
                    s.ownerId = :viewerId
                    OR s.scope = 'PUBLIC'
                    OR s.scope = 'OFFICIAL'
                    OR (
                      s.scope = 'FRIENDS'
                      AND EXISTS (
                        SELECT 1 FROM UserFriend uf
                        WHERE uf.user.id = s.ownerId
                          AND uf.friend.id = :viewerId
                          AND uf.status = 'ACCEPTED'
                      )
                    )
                  )
                ORDER BY s.createdAt DESC
            """)
    List<Spot> findRecentVisibleSpots(@Param("viewerId") Long viewerId, Pageable pageable);

    @Query("""
                SELECT s FROM Spot s
                LEFT JOIN s.posts p
                WHERE s.useYn = 'Y'
                  AND (
                    s.ownerId = :viewerId
                    OR s.scope = 'PUBLIC'
                    OR s.scope = 'OFFICIAL'
                    OR (
                      s.scope = 'FRIENDS'
                      AND EXISTS (
                        SELECT 1 FROM UserFriend uf
                        WHERE uf.user.id = s.ownerId
                          AND uf.friend.id = :viewerId
                          AND uf.status = 'ACCEPTED'
                      )
                    )
                  )
                GROUP BY s
                ORDER BY COUNT(p) DESC
            """)
    List<Spot> findPopularVisibleSpots(@Param("viewerId") Long viewerId, Pageable pageable);

}