package com.example.kolos.service.impl;

import com.example.kolos.repository.RegionRepository;
import com.example.kolos.service.RegionService;
import com.example.kolos.model.Region;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // Убедитесь, что импорт есть
import java.util.Collections; // Добавим для возврата пустого списка

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    // Изменено: теперь ищет ТОЧНОЕ совпадение, так как метод Containing удален из репозитория
    // Также можно переименовать этот метод в findRegionByExactName(String nameRegion)
    // в интерфейсе и здесь, чтобы отразить изменение в поведении.
    public List<Region> findRegionsByName(String nameRegion) {
        // Проверка на null или пустое значение остается
        if (nameRegion == null || nameRegion.trim().isEmpty()) {
            throw new IllegalArgumentException("Region name cannot be null or empty.");
        }
        // Используем findByNameRegion для ТОЧНОГО поиска.
        // Поскольку он возвращает Optional, преобразуем его в List.
        Optional<Region> regionOptional = regionRepository.findByNameRegion(nameRegion);

        // Изменение: Вместо бросания исключения, возвращаем пустой список, если регион не найден
        return regionOptional.map(Collections::singletonList).orElse(Collections.emptyList());
    }

    @Override
    public List<Region> findAll() {
        // Возвращаем все регионы
        return regionRepository.findAll();
    }

    @Override
    public Optional<Region> findById(Long idRegion) {
        // Проверка на null для idRegion
        if (idRegion == null) {
            throw new IllegalArgumentException("Region ID cannot be null.");
        }
        // Проверяем, существует ли регион с таким id
        return regionRepository.findById(idRegion);
    }

    @Override
    public Region save(Region region) {
        if (region == null || region.getNameRegion() == null || region.getNameRegion().trim().isEmpty()) {
            throw new IllegalArgumentException("Region data and name cannot be null or empty.");
        }

        // Логика проверки уникальности:
        Optional<Region> existingRegionWithSameName = regionRepository.findByNameRegion(region.getNameRegion());

        if (region.getIdRegion() == null) { // Сценарий создания новой сущности
            if (existingRegionWithSameName.isPresent()) {
                throw new IllegalArgumentException("Region with name '" + region.getNameRegion() + "' already exists.");
            }
        } else { // Сценарий обновления существующей сущности
            if (existingRegionWithSameName.isPresent() && !existingRegionWithSameName.get().getIdRegion().equals(region.getIdRegion())) {
                // Если найден регион с таким же именем, но другим ID, значит, имя не уникально для обновления.
                throw new IllegalArgumentException("Region with name '" + region.getNameRegion() + "' already exists for another region.");
            }
            // Также убедимся, что обновляемая сущность вообще существует
            if (!regionRepository.existsById(region.getIdRegion())) {
                throw new IllegalArgumentException("Region with ID " + region.getIdRegion() + " not found for update.");
            }
        }
        return regionRepository.save(region);
    }
}