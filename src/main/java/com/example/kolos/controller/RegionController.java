package com.example.kolos.controller;

import com.example.kolos.model.Region;
import com.example.kolos.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<Region>> getRegionsByName(@RequestParam String name) { // <--- Теперь возвращает List<Region>
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Возвращаем 400, если имя не указано
        }
        List<Region> regions = regionService.findRegionsByName(name);
        // Если список пуст, возвращаем 204 No Content. Иначе - 200 OK с найденным списком.
        return regions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(regions);
    }

    @PostMapping
    public ResponseEntity<Region> createRegion(@RequestBody Region region) {
        if (region == null || region.getNameRegion() == null || region.getNameRegion().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (region.getIdRegion() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        Region savedRegion = regionService.save(region);
        return ResponseEntity.created(URI.create("/regions/" + savedRegion.getIdRegion())).body(savedRegion);
    }
}