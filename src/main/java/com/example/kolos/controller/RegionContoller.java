package com.example.kolos.controller;
import com.example.kolos.service.RegionService;
import org.springframework.stereotype.Controller;

@Controller
public class RegionContoller {
    private final RegionService regionService;

    public RegionContoller(RegionService regionService) {
        this.regionService = regionService;
    }
}
