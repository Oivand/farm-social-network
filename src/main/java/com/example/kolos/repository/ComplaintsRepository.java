package com.example.kolos.repository;

import com.example.kolos.model.Complaints;
import com.example.kolos.model.KindsComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintsRepository extends JpaRepository<Complaints, Long> {
    // найти нарушение по его айди
    List<Complaints> findByIdComplaint(Long idComplaint);

    //найти нарушения по виду
    List<Complaints> findBykindСomplaint (KindsComplaint kindСomplaint);

    //найти нарушение по описанию
    List<Complaints> findByDescriptionComplaint(String descriptionComplaint);

    //найти по нарушителю
    List<Complaints> findByIdAccused(int idAccused);

    //найти по дате создания объявления о нарушении
    List<Complaints> findTop10ByOrderByCreatedAtDesc();


}
