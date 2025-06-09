package com.example.kolos.service;

import com.example.kolos.model.Complaints;

import java.util.List;
import java.util.Optional;

public interface ComplaintsService {

    // Найти жалобы по описанию (поиск подстроки)
    List<Complaints> findComplaintsByDescriptionContaining(String description);

    // Найти жалобы по обвиняемому по его ID
    List<Complaints> findComplaintsByAccusedId(Long accusedId);

    // Найти жалобы по истцу по его ID
    List<Complaints> findComplaintsByAccuserId(Long accuserId);

    // Найти 10 последних жалоб
    List<Complaints> findTop10ComplaintsOrderByCreatedAtDesc();

    // Найти жалобы по типу (виду) жалобы по его ID
    List<Complaints> findComplaintsByKindId(Long kindComplaintId);

    // Найти жалобу по ID
    Optional<Complaints> findById(Long complaintId);

    // Получить все жалобы
    List<Complaints> findAll();

    // Сохранить новую жалобу
    Complaints save(Complaints complaints);

    // Обновить существующую жалобу
    Complaints update(Long complaintId, Complaints updatedComplaints);

    // Удалить жалобу по ID
    void delete(Long complaintId);
}