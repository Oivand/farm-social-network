package com.example.kolos.service;

import com.example.kolos.model.Publication;
import com.example.kolos.repository.PublicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PublicationService {
    private final PublicationsRepository publicationsRepository;
    /*
    * Здесь @Autowired внедряет репозиторий PublicationRepository в сервис через конструктор.
    * Это позволяет использовать его для взаимодействия с базой данных.
    */
    @Autowired
    public PublicationService(PublicationsRepository publicationsRepository) {
        this.publicationsRepository = publicationsRepository;
    }

    }


