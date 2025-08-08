package com.snowball.snowball.service;

import com.snowball.snowball.dto.SpotDto;
import com.snowball.snowball.entity.Spot;
import com.snowball.snowball.repository.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.snowball.snowball.dto.SpotDto;


import java.util.List;
import java.util.Optional;

@Service
public class SpotService {
    private final SpotRepository spotRepository;

    public SpotService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    public Spot save(Spot spot) {
        return spotRepository.save(spot);
    }

    public Spot findById(Long id) {
        return spotRepository.findById(id).orElse(null);
    }

    // 내 맵: ownerId=viewerId (scope 무시)
    public List<Spot> findByOwnerIdAndUseYn(Long ownerId, String useYn) {
        return spotRepository.findByOwnerIdAndUseYn(ownerId, useYn);
    }

    public List<Spot> findByScopeAndUseYn(String scope, String useYn) {
        return spotRepository.findByScopeAndUseYn(scope, useYn);
    }

    // 남의 맵: PUBLIC/OFFICIAL/(FRIENDS+친구만) 반환
    public List<Spot> findVisibleSpotsForViewer(Long ownerId, Long viewerId) {
        return spotRepository.findVisibleSpotsForViewer(ownerId, viewerId);
    }

    public Optional<Spot> findByIdAndUseYn(Long id, String useYn) {
        return spotRepository.findByIdAndUseYn(id, useYn);
    }

    public List<Spot> findPopularSpots(Pageable pageable) {
        return spotRepository.findPopularSpots(pageable);
    }

    public List<Spot> findByUseYn(String useYn) {
        return spotRepository.findByUseYn(useYn);
    }

    public List<Spot> findByUseYn(String useYn, Pageable pageable) {
        return spotRepository.findByUseYn(useYn, pageable);
    }

    // 검색: viewerId 기준 가시권 내 스팟만
    public List<Spot> searchSpotVisibleToUser(String q, Long viewerId, Pageable pageable) {
        return spotRepository.searchSpotVisibleToUser(q, viewerId, pageable);
    }

    public List<Spot> findRecentVisibleSpots(Long viewerId, Pageable pageable) {
        return spotRepository.findRecentVisibleSpots(viewerId, pageable);
    }

    public List<Spot> findPopularVisibleSpots(Long viewerId, Pageable pageable) {
        return spotRepository.findPopularVisibleSpots(viewerId, pageable);
    }

    public List<SpotDto> findRecentVisibleSpotsWithOwnerNickname(Long viewerId, Pageable pageable) {
        return spotRepository.findRecentVisibleSpotsWithOwnerNickname(viewerId, pageable);
    }

    public List<SpotDto> findPopularVisibleSpotsWithOwnerNickname(Long viewerId, Pageable pageable) {
        return spotRepository.findPopularVisibleSpotsWithOwnerNickname(viewerId, pageable);
    }
}