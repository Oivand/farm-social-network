package com.example.kolos.repository;

import com.example.kolos.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{
    // найти все региора по айди
    List<Region> findByIdRegion(Long idRegion);

    //найти регионы по назанию
    List<Region> findByNameRegion(String nameRegion);

}
