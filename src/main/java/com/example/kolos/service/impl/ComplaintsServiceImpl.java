package com.example.kolos.service.impl;

import com.example.kolos.model.Complaints;
import com.example.kolos.model.KindsComplaint;
import com.example.kolos.model.User;
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
        return complaintsRepository.findByDescriptionComplaintContaining(descriptionComplaint);
    }

    @Override
    public List<Complaints> findComplaintsByAccused(User idAccused) {
        return complaintsRepository.findByIdAccused(idAccused);
    }

    @Override
    public List<Complaints> findComplaintsByAccuser(User idAccuser) {
        return complaintsRepository.findByIdAccuser(idAccuser);
    }

    @Override
    public List<Complaints> findComplaints10Latest() {
        return complaintsRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public List<Complaints> findComplaintsByKind(KindsComplaint kindComplaint) {
        return complaintsRepository.findByKindComplaint(kindComplaint);
    }

    @Override
    public Optional<Complaints> findById(Long idComplaint) {
        return complaintsRepository.findById(idComplaint);
    }

    @Override
    public List<Complaints> findAll() {
        return complaintsRepository.findAll();
    }
}
