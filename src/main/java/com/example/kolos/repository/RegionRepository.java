package com.example.kolos.repository;

import com.example.kolos.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{
    // найти все региора по айди
    Optional<Region> findById(Long idRegion);

    //найти регионы по назанию
    List<Region> findByNameRegionContaining(String nameRegion);

    List<Region> findAll();

}
