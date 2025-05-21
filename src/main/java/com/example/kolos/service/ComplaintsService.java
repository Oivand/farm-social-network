package com.example.kolos.service;

import com.example.kolos.model.Complaints;

import java.util.List;
import java.util.Optional;

public interface ComplaintsService {

    // Найти жалобы по описанию (поиск подстроки)
    List<Complaints> findComplaintsByDescription(String descriptionComplaint);

    // Найти жалобы по обвиняемому
    List<Complaints> findComplaintsByAccusedId(Long idAccused);

    // Найти жалобы по истцу
    List<Complaints> findComplaintsByAccuser(Long idAccuser);

    // Найти 10 последних жалоб
    List<Complaints> findComplaints10Latest();

    List<Complaints> findComplaintsByKind(Long kindComplaint);

    Optional<Complaints> findById(Long idComplaint);

    List<Complaints> findAll();
}
