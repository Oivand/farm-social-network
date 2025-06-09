package com.example.kolos.service;

import com.example.kolos.model.KindsComplaint;

import java.util.List;
import java.util.Optional;

public interface KindsComplaintService {

    // Changed to reflect exact search
    // It's more common to return Optional<KindsComplaint> if you expect a single unique result
    // but we can stick to List<KindsComplaint> for consistency with controller's /search endpoint if desired.
    // I'll keep List<KindsComplaint> for consistency with how RegionService handles this for now.
    List<KindsComplaint> findKindByName(String nameKindComplaint); // Renamed from findKindsByNameContaining

    // Get all complaint kinds
    List<KindsComplaint> findAll();

    // Find complaint kind by ID
    Optional<KindsComplaint> findById(Long id);

    // Save a new or updated complaint kind
    KindsComplaint save(KindsComplaint kindComplaint);

    // Delete a complaint kind
    void delete(Long id); // Added the delete method
}