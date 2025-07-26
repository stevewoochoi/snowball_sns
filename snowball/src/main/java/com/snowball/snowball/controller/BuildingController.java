

package com.snowball.snowball.controller;

import com.snowball.snowball.entity.Building;
import com.snowball.snowball.config.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping
    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }
}