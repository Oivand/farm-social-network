package com.example.kolos.service.impl;

import com.example.kolos.interfaces.SectorInterface;
import com.example.kolos.model.Sector;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorServiceImpl implements SectorInterface {
    @Override
    public List<Sector> findSectorByName(String nameSector) {
        return List.of();
    }
}
