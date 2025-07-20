package com.snowball.snowball.controller;

import com.snowball.snowball.entity.Spot;
import com.snowball.snowball.entity.Building;
import com.snowball.snowball.entity.Category;
import com.snowball.snowball.service.SpotService;
import com.snowball.snowball.repository.BuildingRepository;
import com.snowball.snowball.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/spots")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://snowball.iuorder.com",
        "https://snowball.iuorder.com"
}, allowedHeaders = "*", allowCredentials = "true")
public class SpotController {
    private final SpotService spotService;
    private final BuildingRepository buildingRepository;
    private final CategoryRepository categoryRepository;

    public SpotController(
            SpotService spotService,
            BuildingRepository buildingRepository,
            CategoryRepository categoryRepository) {
        this.spotService = spotService;
        this.buildingRepository = buildingRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Spot> getSpots() {
        return spotService.findAll();
    }

    @PostMapping
    public Spot createSpot(
            @RequestBody Spot spot,
            @RequestParam Long buildingId,
            @RequestParam Long categoryId,
            @RequestParam(name = "owner_id", required = false) Long ownerId,
            @RequestParam(defaultValue = "PRIVATE") String scope) {
        spot.setBuilding(buildingRepository.findById(buildingId).orElse(null));
        spot.setCategory(categoryRepository.findById(categoryId).orElse(null));
        spot.setOwnerId(ownerId); // 프론트에서 받은 ownerId 저장!
        spot.setScope(scope);
        spot.setCreatedAt(LocalDateTime.now());
        return spotService.save(spot);
    }

    @GetMapping("/{id}")
    public Spot getSpot(@PathVariable Long id) {
        return spotService.findById(id);
    }

}