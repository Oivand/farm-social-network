package com.example.kolos.service.impl;

import com.example.kolos.model.Complaints;
import com.example.kolos.repository.ComplaintsRepository;
import com.example.kolos.service.ComplaintsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintsServiceImpl implements ComplaintsService {

    private final ComplaintsRepository complaintsRepository;

    public ComplaintsServiceImpl(ComplaintsRepository complaintsRepository) {
        this.complaintsRepository = complaintsRepository;
    }

    @Override
    public List<Complaints> findComplaintsByDescription(String descriptionComplaint) {
        if (descriptionComplaint == null || descriptionComplaint.isEmpty()) {
            throw new IllegalArgumentException("Описание жалобы не может быть пустым.");
        }
        return complaintsRepository.findByDescriptionComplaintContaining(descriptionComplaint);
    }

    @Override
    public List<Complaints> findComplaintsByAccusedId(Long idAccused) {
        if (idAccused == null) {
            throw new IllegalArgumentException("ID обвиняемого не может быть null.");
        }
        return complaintsRepository.findByIdAccused(idAccused);
    }



    @Override
    public List<Complaints> findComplaintsByAccuser(Long idAccuser) {
        if (idAccuser == null) {
            throw new IllegalArgumentException("Должен быть указан истец.");
        }
        return complaintsRepository.findByIdAccuser(idAccuser);
    }

    @Override
    public List<Complaints> findComplaints10Latest() {
        List<Complaints> latestComplaints = complaintsRepository.findTop10ByOrderByCreatedAtDesc();
        if (latestComplaints.isEmpty()) {
            throw new IllegalStateException("Не найдено ни одной жалобы.");
        }
        return latestComplaints;
    }

    @Override
    public List<Complaints> findComplaintsByKind(Long kindComplaint) {
        if (kindComplaint == null) {
            throw new IllegalArgumentException("Тип жалобы не может быть пустым.");
        }
        return complaintsRepository.findByKindComplaint(kindComplaint);
    }

    @Override
    public Optional<Complaints> findById(Long idComplaint) {
        if (idComplaint == null) {
            throw new IllegalArgumentException("ID жалобы не может быть пустым.");
        }
        Optional<Complaints> complaint = complaintsRepository.findById(idComplaint);
        if (complaint.isEmpty()) {
            throw new IllegalStateException("Жалоба с указанным ID не найдена.");
        }
        return complaint;
    }

    @Override
    public List<Complaints> findAll() {
        List<Complaints> allComplaints = complaintsRepository.findAll();
        if (allComplaints.isEmpty()) {
            throw new IllegalStateException("В базе данных отсутствуют жалобы.");
        }
        return allComplaints;
    }

}
