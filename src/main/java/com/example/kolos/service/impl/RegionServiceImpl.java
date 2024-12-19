package com.example.kolos.service.impl;

import com.example.kolos.repository.RegionRepository;
import com.example.kolos.service.RegionService;
import com.example.kolos.model.Region;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public List<Region> findRegionsByName(String nameRegion) {
        return regionRepository.findByNameRegionContaining(nameRegion);
    }

    @Override
    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    @Override
    public Optional<Region> findById(Long idRegion) {
        return regionRepository.findById(idRegion);
    }
}
