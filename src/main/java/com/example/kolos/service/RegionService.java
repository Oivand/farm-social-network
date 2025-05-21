package com.example.kolos.service;

import java.util.List;
import java.util.Optional;

import com.example.kolos.model.Region;

public interface RegionService {

    // Найти регионы по названию (с частичным совпадением)
    List<Region> findRegionsByName(String nameRegion);

    // Получить все регионы
    List<Region> findAll();

    // Найти регион по ID
    Optional<Region> findById(Long idRegion);

    // Сохранить новый или обновленный регион
    Region save(Region region);
}
