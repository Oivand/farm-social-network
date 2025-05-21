package com.example.kolos.controller;

import com.example.kolos.model.Region;
import com.example.kolos.service.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public List<Region> getAllRegions() {
        return regionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        return regionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Region>> getRegionsByName(@RequestParam String region) {
        if (region == null || region.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Возвращаем 400, если имя не указано
        }
        List<Region> regions = regionService.findRegionsByName(region);
        return regions.isEmpty() ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(regions);
    }

    @PostMapping
    ResponseEntity<Region> addKind(@RequestBody Region region) {
        Region saved = regionService.save(region);
        return ResponseEntity.ok(saved);

    }
}