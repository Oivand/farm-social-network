package com.example.kolos.service.impl;

import com.example.kolos.model.KindsComplaint;
import com.example.kolos.repository.KindsComplaintRepository;
import com.example.kolos.service.KindsComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KindsComplaintServiceImpl implements KindsComplaintService {

   private final KindsComplaintRepository kindsComplaintRepository;

    @Autowired
    public KindsComplaintServiceImpl(KindsComplaintRepository kindsComplaintRepository) {
        this.kindsComplaintRepository = kindsComplaintRepository;
    }

    @Override
    public Optional<KindsComplaint> findById(Long id) {
        return kindsComplaintRepository.findById(id);
    }

    @Override
    public List<KindsComplaint> findAll() {
        return kindsComplaintRepository.findAll();
    }

    @Override
    public List<KindsComplaint> getKindComplaintsByName(String nameKindComplaint) {
        return kindsComplaintRepository.findByNameKindComplaintContainingOrderByNameKindComplaint(nameKindComplaint);
    }
}
