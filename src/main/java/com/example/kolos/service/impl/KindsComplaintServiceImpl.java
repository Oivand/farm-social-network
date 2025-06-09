package com.example.kolos.service.impl;

import com.example.kolos.model.KindsComplaint;
import com.example.kolos.repository.KindsComplaintRepository;
import com.example.kolos.service.KindsComplaintService;
import org.springframework.stereotype.Service;

import java.util.Collections; // For Collections.singletonList and emptyList
import java.util.List;
import java.util.Optional;

@Service
public class KindsComplaintServiceImpl implements KindsComplaintService {

    private final KindsComplaintRepository kindsComplaintRepository;

    public KindsComplaintServiceImpl(KindsComplaintRepository kindsComplaintRepository) { // Removed @Autowired
        this.kindsComplaintRepository = kindsComplaintRepository;
    }

    @Override
    public Optional<KindsComplaint> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Ошибка: ID вида жалобы не может быть null.");
        }
        return kindsComplaintRepository.findById(id);
    }

    @Override
    public List<KindsComplaint> findAll() {
        return kindsComplaintRepository.findAll();
    }

    @Override
    // Now performs an exact search and returns a list (0 or 1 element)
    public List<KindsComplaint> findKindByName(String nameKindComplaint) { // Renamed from getKindComplaintsByName
        if (nameKindComplaint == null || nameKindComplaint.trim().isEmpty()) { // Added trim()
            throw new IllegalArgumentException("Ошибка: название вида жалобы не может быть null или пустым.");
        }
        // Use exact findByNameKindComplaint and convert to List
        Optional<KindsComplaint> kindOptional = kindsComplaintRepository.findByNameKindComplaint(nameKindComplaint);
        return kindOptional.map(Collections::singletonList).orElse(Collections.emptyList());
    }

    @Override
    public KindsComplaint save(KindsComplaint kind) {
        if (kind == null || kind.getNameKindComplaint() == null || kind.getNameKindComplaint().trim().isEmpty()) { // Added trim()
            throw new IllegalArgumentException("Данные вида жалобы и название не могут быть null или пустыми.");
        }

        // Check for unique name
        Optional<KindsComplaint> existingKindWithSameName = kindsComplaintRepository.findByNameKindComplaint(kind.getNameKindComplaint());

        if (kind.getIdKindComplaint() == null) { // Creating a new kind
            if (existingKindWithSameName.isPresent()) {
                throw new IllegalArgumentException("Вид жалобы с названием '" + kind.getNameKindComplaint() + "' уже существует.");
            }
        } else { // Updating an existing kind
            if (existingKindWithSameName.isPresent() && !existingKindWithSameName.get().getIdKindComplaint().equals(kind.getIdKindComplaint())) {
                throw new IllegalArgumentException("Вид жалобы с названием '" + kind.getNameKindComplaint() + "' уже существует для другого ID.");
            }
            // Ensure the kind being updated actually exists
            if (!kindsComplaintRepository.existsById(kind.getIdKindComplaint())) {
                throw new IllegalArgumentException("Вид жалобы с ID " + kind.getIdKindComplaint() + " не найден для обновления.");
            }
        }
        return kindsComplaintRepository.save(kind);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID вида жалобы не может быть null.");
        }
        // Check if the kind exists before attempting to delete
        if (!kindsComplaintRepository.existsById(id)) {
            throw new IllegalArgumentException("Вид жалобы с ID " + id + " не найден для удаления.");
        }
        kindsComplaintRepository.deleteById(id);
    }
}