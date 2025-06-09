package com.example.kolos.controller;
import com.example.kolos.model.Sector;
import com.example.kolos.service.SectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sectors")
public class SectorContoller {
    private final SectorService sectorService;

    public SectorContoller(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping
    public List<Sector> getAllSectors(){
        return sectorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sector> getSectorById(@PathVariable Long id){
        return sectorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Sector>> getSectorByName(@RequestParam String sector){
        if(sector == null || sector.isEmpty()){
            return ResponseEntity.badRequest().build(); //400
        }
        List<Sector> sectors = sectorService.findSectorByName(sector);
        return sectors.isEmpty() ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(sectors);
    }
}
