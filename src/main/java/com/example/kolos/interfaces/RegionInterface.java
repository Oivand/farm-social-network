package com.example.kolos.interfaces;

import java.util.List;

import com.example.kolos.model.Region;

public interface RegionInterface {

    List<Region> findRegionsByName(String nameRegion);
}
