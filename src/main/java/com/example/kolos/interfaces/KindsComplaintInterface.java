package com.example.kolos.interfaces;

import com.example.kolos.model.KindsComplaint;

import java.util.List;

public interface KindsComplaintInterface {

    List<KindsComplaint> getKindComplaintsByName(String nameKindComplaint);
}
