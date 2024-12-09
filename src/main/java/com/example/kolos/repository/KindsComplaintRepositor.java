package com.example.kolos.repository;

import com.example.kolos.model.KindsComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KindsComplaintRepositor extends JpaRepository<KindsComplaint, Long> {
}


