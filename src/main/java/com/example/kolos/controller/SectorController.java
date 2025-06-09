package com.example.kolos.controller;

import com.example.kolos.model.Sector;
import com.example.kolos.service.SectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sectors")
public class SectorController { // <--- ИЗМЕНЕНО: Исправлена опечатка 'Contoller' на 'Controller'
    private final SectorService sectorService;

    public SectorController(SectorService sectorService) {
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
        // Эта проверка остается здесь, так как она относится к валидации входных параметров запроса
        // IllegalArgumentException из сервиса будет обработан GlobalExceptionHandler
        if(sector == null || sector.isEmpty()){
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        List<Sector> sectors = sectorService.findSectorByName(sector);
        return sectors.isEmpty() ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(sectors);
    }
}