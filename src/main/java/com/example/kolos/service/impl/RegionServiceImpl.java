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
        // Проверка на null или пустое значение
        if (nameRegion == null || nameRegion.trim().isEmpty()) {
            throw new IllegalArgumentException("Region name cannot be null or empty.");
        }
        return regionRepository.findByNameRegionContaining(nameRegion);
    }

    @Override
    public List<Region> findAll() {
        // Возвращаем все регионы, здесь проверки не требуются
        return regionRepository.findAll();
    }

    @Override
    public Optional<Region> findById(Long idRegion) {
        // Проверка на null для idRegion
        if (idRegion == null) {
            throw new IllegalArgumentException("Region ID cannot be null.");
        }
        return regionRepository.findById(idRegion);
    }

    @Override
    public Region save(Region region){
        return regionRepository.save(region);
    }
}
