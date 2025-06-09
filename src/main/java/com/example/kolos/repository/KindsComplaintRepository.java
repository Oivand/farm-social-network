package com.example.kolos.repository;

import com.example.kolos.model.KindsComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KindsComplaintRepository extends JpaRepository<KindsComplaint, Long> {
    Optional<KindsComplaint> findByNameKindComplaint(String nameKindComplaint);
}


