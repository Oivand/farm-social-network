package com.example.kolos.service;

import com.example.kolos.model.Complaints;
import com.example.kolos.model.KindsComplaint;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface ComplaintsService {

    // Найти жалобы по описанию (поиск подстроки)
    List<Complaints> findComplaintsByDescription(String descriptionComplaint);

    // Найти жалобы по обвиняемому
    List<Complaints> findComplaintsByAccused(User idAccused);

    // Найти жалобы по истцу
    List<Complaints> findComplaintsByAccuser(User idAccuser);

    // Найти 10 последних жалоб
    List<Complaints> findComplaints10Latest();

    List<Complaints> findComplaintsByKind(KindsComplaint kindComplaint);

    Optional<Complaints> findById(Long idComplaint);

    List<Complaints> findAll();

}
