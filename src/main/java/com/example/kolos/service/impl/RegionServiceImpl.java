package com.example.kolos.service.impl;

import com.example.kolos.interfaces.RegionInterface;
import com.example.kolos.model.Region;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionInterface {
    @Override
    public List<Region> findRegionsByName(String nameRegion) {
        return List.of();
    }
}
