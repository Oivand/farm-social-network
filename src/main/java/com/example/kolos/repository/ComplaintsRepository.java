package com.example.kolos.repository;

import com.example.kolos.model.Complaints;
import com.example.kolos.model.KindsComplaint;
import com.example.kolos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintsRepository extends JpaRepository<Complaints, Long> {

/**
 * Найти жалобы по виду нарушения
 * ПОФИКСИТЬ ЭФЭАБФьМЩВЬЩИЯЗЩИЬЩЬИЗАВЬИ ЬАДИДЖАБ
 */
List<Complaints> findBykindComplaint(KindsComplaint kindComplaint);

    // Найти жалобы по описанию (поиск подстроки)
    List<Complaints> findByDescriptionComplaintContaining(String descriptionComplaint);

    // Найти жалобы по обвиняемому
    List<Complaints> findByIdAccused(User idAccused);

    // Найти жалобы по истцу
    List<Complaints> findByIdAccuser(User idAccuser);

    // Найти 10 последних жалоб
    List<Complaints> findTop10ByOrderByCreatedAtDesc();

}



