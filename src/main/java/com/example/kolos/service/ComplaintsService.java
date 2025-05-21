package com.example.kolos.service;

import com.example.kolos.model.Complaints;

import java.util.List;
import java.util.Optional;

public interface ComplaintsService {

    // Найти жалобы по описанию (поиск подстроки)
    List<Complaints> findComplaintsByDescriptionContaining(String description);

    // Найти жалобы по обвиняемому
    List<Complaints> findComplaintsByAccusedId(Long accusedId);

    // Найти жалобы по истцу
    List<Complaints> findComplaintsByAccuserId(Long accuserId);

    // Найти 10 последних жалоб
    List<Complaints> findTop10ComplaintsOrderByCreatedAtDesc();

    // Найти жалобы по типу (виду) жалобы
    List<Complaints> findComplaintsByKindId(Long kindComplaintId);

    // Найти жалобу по ID
    Optional<Complaints> findById(Long complaintId);

    // Получить все жалобы
    List<Complaints> findAll();

    //сохранить новую жалобу
    Complaints save(Complaints complaints);
}
