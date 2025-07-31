package com.snowball.snowball.controller;

import com.snowball.snowball.entity.Spot;
import com.snowball.snowball.entity.Building;
import com.snowball.snowball.entity.Category;
import com.snowball.snowball.entity.SpotBoard;
import com.snowball.snowball.service.SpotService;
import com.snowball.snowball.repository.BuildingRepository;
import com.snowball.snowball.repository.CategoryRepository;
import com.snowball.snowball.repository.SpotBoardRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    private final SpotBoardRepository spotBoardRepository;

    public SpotController(
            SpotService spotService,
            BuildingRepository buildingRepository,
            CategoryRepository categoryRepository,
            SpotBoardRepository spotBoardRepository) {
        this.spotService = spotService;
        this.buildingRepository = buildingRepository;
        this.categoryRepository = categoryRepository;
        this.spotBoardRepository = spotBoardRepository;
    }

    // useYn="Y"인 스팟만 조회, ownerId와 scope로 필터링 가능
    @GetMapping
    public List<Spot> getSpots(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String scope) {
        if (ownerId != null && scope != null) {
            return spotService.findByOwnerIdAndScopeAndUseYn(ownerId, scope, "Y");
        } else {
            return spotService.findByUseYn("Y");
        }
    }

    @PostMapping
    public Spot createSpot(
            @RequestBody Spot spot,
            @RequestParam Long buildingId,
            @RequestParam Long categoryId,
            @RequestParam(name = "ownerId", required = false) Long ownerId,
            @RequestParam(defaultValue = "PRIVATE") String scope) {
        spot.setBuilding(buildingRepository.findById(buildingId).orElse(null));
        spot.setCategory(categoryRepository.findById(categoryId).orElse(null));
        spot.setOwnerId(ownerId); // 프론트에서 받은 ownerId 저장!
        spot.setScope(scope);
        spot.setCreatedAt(LocalDateTime.now());
        Spot savedSpot = spotService.save(spot);

        // 게시판 자동 생성
        SpotBoard board = new SpotBoard();
        board.setSpot(savedSpot);
        board.setName("GUEST BOOK");
        board.setCreatedAt(LocalDateTime.now());
        spotBoardRepository.save(board);

        return savedSpot;
    }

    // useYn="Y"인 단건 조회만 허용
    @GetMapping("/{id}")
    public ResponseEntity<Spot> getSpot(@PathVariable Long id) {
        return spotService.findByIdAndUseYn(id, "Y")
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 논리삭제(soft delete) API - 오너만 삭제 가능
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpot(
            @PathVariable Long id,
            @RequestAttribute(value = "user", required = false) com.snowball.snowball.entity.User user) {
        return spotService.findByIdAndUseYn(id, "Y")
                .map(spot -> {
                    if (user == null || !spot.getOwnerId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this spot.");
                    }
                    spot.setUseYn("N");
                    spotService.save(spot);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Spot not found"));
    }

    // 위치 등 Spot 정보 부분 업데이트 (lat/lng만 패치할 때도 활용 가능)
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateSpot(
            @PathVariable Long id,
            @RequestBody Spot updatedSpot,
            @RequestAttribute(value = "user", required = false) com.snowball.snowball.entity.User user) {
        Spot spot = spotService.findById(id); // ✅
        if (spot == null)
            return ResponseEntity.notFound().build();
        // 오너만 변경 가능
        if (user == null || !spot.getOwnerId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this spot.");
        }

        // lat/lng 등 필요한 필드만 업데이트
        if (updatedSpot.getLat() != null)
            spot.setLat(updatedSpot.getLat());
        if (updatedSpot.getLng() != null)
            spot.setLng(updatedSpot.getLng());
        // 필요시 이름, 아이콘 등도 추가로 업데이트 가능

        spotService.save(spot);
        return ResponseEntity.ok(spot);
    }

}