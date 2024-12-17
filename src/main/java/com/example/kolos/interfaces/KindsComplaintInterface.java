package com.example.kolos.interfaces;

import com.example.kolos.model.KindsComplaint;

import java.util.List;

public interface KindsComplaintInterface {
    List<KindsComplaint> getById(Long idKindComplaint);

    List<KindsComplaint> getAll();

    List<KindsComplaint> getByName(String nameKindComplaint);
}
