package com.example.kolos.controller;
import com.example.kolos.service.SectorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sectors")
public class SectorContoller {
    private final SectorService sectorService;

    public SectorContoller(SectorService sectorService) {
        this.sectorService = sectorService;
    }
}
