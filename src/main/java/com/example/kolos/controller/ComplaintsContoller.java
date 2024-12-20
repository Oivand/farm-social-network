package com.example.kolos.controller;

import com.example.kolos.service.ComplaintsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/complaints")
public class ComplaintsContoller {
    private final ComplaintsService complaintsService;

    public ComplaintsContoller(ComplaintsService complaintsService) {
        this.complaintsService = complaintsService;
    }
}
