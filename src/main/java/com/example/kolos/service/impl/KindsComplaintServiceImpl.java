package com.example.kolos.service.impl;

import com.example.kolos.interfaces.KindsComplaintInterface;
import com.example.kolos.model.KindsComplaint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KindsComplaintServiceImpl implements KindsComplaintInterface {
    @Override
    public List<KindsComplaint> getKindComplaintsByName(String nameKindComplaint) {
        return List.of();
    }
}
