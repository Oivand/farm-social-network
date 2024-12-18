package com.example.kolos.service.impl;

import com.example.kolos.interfaces.ComplaintsInterface;
import com.example.kolos.model.Complaints;
import com.example.kolos.model.KindsComplaint;
import com.example.kolos.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintsServiceImpl implements ComplaintsInterface {
    @Override
    public List<Complaints> findComplaintsByDescription(String descriptionComplaint) {
        return List.of();
    }

    @Override
    public List<Complaints> findComplaintsByAccused(User idAccused) {
        return List.of();
    }

    @Override
    public List<Complaints> findComplaintsByAccuser(User idAccuser) {
        return List.of();
    }

    @Override
    public List<Complaints> findComplaints10Latest() {
        return List.of();
    }

    @Override
    public List<Complaints> findComplaintsByKind(KindsComplaint kindComplaint) {
        return List.of();
    }
}
