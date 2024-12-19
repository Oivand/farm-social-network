package com.example.kolos.service;

import java.util.List;
import java.util.Optional;

import com.example.kolos.model.Region;

public interface RegionService {

    List<Region> findRegionsByName(String nameRegion);

    List<Region> findAll();

    Optional<Region> findById(Long idRegion);
}
