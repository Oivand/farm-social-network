package com.example.kolos.interfaces;

import java.util.List;
import java.util.Optional;

import com.example.kolos.model.Region;

public interface RegionServiceInterface {

    List<Region> findRegionsByName(String nameRegion);
}
