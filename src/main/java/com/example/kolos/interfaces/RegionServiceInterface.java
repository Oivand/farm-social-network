package com.example.kolos.interfaces;

import java.util.List;
import java.util.Optional;

import com.example.kolos.model.Region;

public interface RegionServiceInterface {
    Optional<Region> getRegionByIdRegion(Long idRegion);

    List<Region> getRegionByNameRegion(String nameRegion);

    List<Region> getAllRegions();

}