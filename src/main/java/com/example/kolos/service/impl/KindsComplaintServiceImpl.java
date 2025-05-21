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
        if (id == null) {
            throw new IllegalArgumentException("Ошибка: ID вида жалобы (id) не может быть null.");
        }
        return kindsComplaintRepository.findById(id);
    }

    @Override
    public List<KindsComplaint> findAll() {
        return kindsComplaintRepository.findAll(); // Предполагаем, что findAll всегда содержит данные
    }

    @Override
    public List<KindsComplaint> getKindComplaintsByName(String nameKindComplaint) {
        if (nameKindComplaint == null || nameKindComplaint.isEmpty()) {
            throw new IllegalArgumentException("Ошибка: название вида жалобы (nameKindComplaint) не может быть null или пустым.");
        }
        return kindsComplaintRepository.findByNameKindComplaintContainingOrderByNameKindComplaint(nameKindComplaint);
    }

    @Override
    public KindsComplaint save(KindsComplaint kind) {
        return kindsComplaintRepository.save(kind);
    }

    @Override
    public void delete(Long id) {
        kindsComplaintRepository.deleteById(id);
    }


}
