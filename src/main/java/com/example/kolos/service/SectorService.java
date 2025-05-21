package com.example.kolos.service;

import com.example.kolos.model.Sector;
import java.util.List;
import java.util.Optional;

public interface SectorService {

    // Найти сектор по названию (с частичным совпадением)
    List<Sector> findSectorByName(String nameSector);

    // Найти сектор по ID
    Optional<Sector> findById(Long idSector);

    // Получить все сектора
    List<Sector> findAll();
}
