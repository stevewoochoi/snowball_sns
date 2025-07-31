package com.snowball.snowball.repository;

import com.snowball.snowball.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    // 기본 CRUD, findByCategory 등 확장 가능

    // 논리삭제(Y)만 조회
    List<Spot> findByUseYn(String useYn);

    // 특정 id + useYn
    Optional<Spot> findByIdAndUseYn(Long id, String useYn);

    List<Spot> findByOwnerIdAndScopeAndUseYn(Long ownerId, String scope, String useYn);

}