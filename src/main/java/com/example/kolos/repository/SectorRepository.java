package com.example.kolos.repository;

import com.example.kolos.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    //найти сектор по айди
    List<Sector> findByIdSector(Long idSector);

    //найти сектор по названию
    List<Sector> findByNameSector(String nameSector);
}
