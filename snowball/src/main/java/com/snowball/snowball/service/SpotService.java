package com.snowball.snowball.service;

import com.snowball.snowball.entity.Spot;
import com.snowball.snowball.repository.SpotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpotService {
    private final SpotRepository spotRepository;

    public SpotService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    public List<Spot> findAll() {
        return spotRepository.findAll();
    }

    public Spot save(Spot spot) {
        return spotRepository.save(spot);
    }

    public Spot findById(Long id) {
        return spotRepository.findById(id).orElse(null);
    }

    // 활성(Y) 스팟만 조회
    public List<Spot> findByUseYn(String useYn) {
        return spotRepository.findByUseYn(useYn);
    }

    // id와 useYn으로 조회 (Optional로 반환)
    public Optional<Spot> findByIdAndUseYn(Long id, String useYn) {
        return spotRepository.findByIdAndUseYn(id, useYn);
    }
    // 특정 owner + scope + 활성화 상태 스팟만 조회
    public List<Spot> findByOwnerIdAndScopeAndUseYn(Long ownerId, String scope, String useYn) {
        return spotRepository.findByOwnerIdAndScopeAndUseYn(ownerId, scope, useYn);
    }
}