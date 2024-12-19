package com.example.kolos.service.impl;

import com.example.kolos.repository.SectorRepository;
import com.example.kolos.service.SectorService;
import com.example.kolos.model.Sector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectorServiceImpl implements SectorService {
    
    private final SectorRepository sectorRepository;

    public SectorServiceImpl(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    @Override
    public List<Sector> findSectorByName(String nameSector) {
        return sectorRepository.findByNameSectorContaining(nameSector);
    }

    @Override
    public Optional<Sector> findById(Long idSector) {
        return sectorRepository.findById(idSector);
    }

    @Override
    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }
}
