package com.example.kolos.service;
import com.example.kolos.interfaces.RegionServiceInterface;
import com.example.kolos.model.Region;
import com.example.kolos.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok создаст конструктор с @Autowired для всех final полей
public class RegionService /*implements RegionServiceInterface {
    private final RegionRepository regionRepository;  // Lombok создаст конструктор для этого поля

    @Override
    public Optional<Region> getRegionByIdRegion(Long idRegion) {
        return regionRepository.findById(idRegion);
    }

    @Override
    public List<Region> getRegionByNameRegion(String nameRegion) {
        return regionRepository.findByNameRegionContaining(nameRegion);
    }

    @Override
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }
}*/{}