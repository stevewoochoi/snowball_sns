package com.snowball.snowball.service;

import com.snowball.snowball.entity.Spot;
import com.snowball.snowball.repository.SpotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}