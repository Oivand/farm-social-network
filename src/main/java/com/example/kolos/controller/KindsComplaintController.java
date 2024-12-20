package com.example.kolos.controller;

import com.example.kolos.service.KindsComplaintService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kindsComplaint")
public class KindsComplaintController {
    private final KindsComplaintService kindsComplaintService;

    public KindsComplaintController(KindsComplaintService kindsComplaintService) {
        this.kindsComplaintService = kindsComplaintService;
    }
}
