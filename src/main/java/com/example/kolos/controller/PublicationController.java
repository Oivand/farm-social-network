package com.example.kolos.controller;
import com.example.kolos.service.PublicationsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publications")
public class PublicationController {
    private final PublicationsService publicationsService;

    public PublicationController(PublicationsService publicationsService) {
        this.publicationsService = publicationsService;
    }
}
