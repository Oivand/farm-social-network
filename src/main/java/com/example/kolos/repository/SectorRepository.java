package com.example.kolos.repository;

import com.example.kolos.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    //List<Sector> findAllByOrderByNameSectorAsc(); вроде не нужно

    //найти сектор по названию
    List<Sector> findByNameSectorContaining(String nameSector);
}