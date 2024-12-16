package com.example.kolos.repository;

import com.example.kolos.model.KindsComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KindsComplaintRepository extends JpaRepository<KindsComplaint, Long> {
    //найти виды нарушений по id
    List<KindsComplaint> findByIdKindComplaint(Long idKindComplaint);

    //найти виды нарушений по названию
    List<KindsComplaint> findByNameKindComplaint(String nameKindComplaint);


}


