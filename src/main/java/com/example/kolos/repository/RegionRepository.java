package com.example.kolos.repository;

import com.example.kolos.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Удаляем List, т.к. он больше не нужен для методов здесь

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{

    // Добавлено: найти регион по ТОЧНОМУ названию
    // Этот метод возвращает Optional<Region>, так как имя должно быть уникальным,
    // и либо будет найден один регион, либо ни одного.
    Optional<Region> findByNameRegion(String nameRegion);
}