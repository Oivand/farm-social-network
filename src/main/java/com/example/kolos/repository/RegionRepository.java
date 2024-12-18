package com.example.kolos.repository;

import com.example.kolos.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{

    //найти регионы по назанию
    List<Region> findByNameRegionContaining(String nameRegion);
}


