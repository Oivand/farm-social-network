package com.example.kolos.service;

import com.example.kolos.model.KindsComplaint;

import java.util.List;
import java.util.Optional;

public interface KindsComplaintService {

    // Найти типы жалоб по названию
    List<KindsComplaint> findKindsByNameContaining(String nameKindComplaint);

    // Получить все типы жалоб
    List<KindsComplaint> findAll();

    // Найти тип жалобы по ID
    Optional<KindsComplaint> findById(Long id);

    // Сохранить новый тип жалобы
    KindsComplaint save(KindsComplaint kindComplaint);
}
