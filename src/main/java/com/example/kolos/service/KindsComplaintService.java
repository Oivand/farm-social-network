package com.example.kolos.service;

import com.example.kolos.model.KindsComplaint;

import java.util.List;
import java.util.Optional;

public interface KindsComplaintService {

    List<KindsComplaint> getKindComplaintsByName(String nameKindComplaint);

    List<KindsComplaint> findAll();

    Optional<KindsComplaint> findById(Long id);
    KindsComplaint save(KindsComplaint kind);
    void delete(Long id);


}
