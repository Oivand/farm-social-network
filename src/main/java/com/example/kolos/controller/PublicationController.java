package com.example.kolos.controller;

import com.example.kolos.service.PublicationService;
import org.springframework.stereotype.Controller;

@Controller
public class PublicationController {
    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }
}
