package com.example.kolos.service;

import com.example.kolos.model.Sector;
import java.util.List;
import java.util.Optional;

public interface SectorService {

    List<Sector> findSectorByName(String nameSector);

    Optional<Sector> findById(Long idSector);

    List<Sector> findAll();
}
