package com.example.kolos.controller;

import com.example.kolos.model.Sector;
import com.example.kolos.service.SectorService;
import org.springframework.stereotype.Controller;

@Controller
public class SectorContoller {

    private final SectorService sectorService;

    public SectorContoller(SectorService sectorService) {
        this.sectorService = sectorService;
    }
}
