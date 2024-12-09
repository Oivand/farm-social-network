package com.example.kolos.repository;

import com.example.kolos.model.Complaints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintsRepository extends JpaRepository<Complaints, Long> {
    // Здесь можно добавить свои методы для поиска
}
