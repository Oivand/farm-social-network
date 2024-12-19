package com.example.kolos.service.impl;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User;
import com.example.kolos.repository.PublicationsRepository;
import com.example.kolos.service.PublicationsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationServiceImpl implements PublicationsService {

    private final PublicationsRepository publicationsRepository;

    public PublicationServiceImpl(PublicationsRepository publicationsRepository) {
        this.publicationsRepository = publicationsRepository;
    }

    @Override
    public List<Publication> find10LatestPublication() {
        return publicationsRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public List<Publication> findPublicationByDescription(String description) {
        return publicationsRepository.findByDescriptionContainingOrderByCreatedAtDesc(description);
    }

    @Override
    public List<Publication> findPublicationByTitle(String title) {
        return publicationsRepository.findByTitleContainingOrderByCreatedAtDesc(title);
    }

    @Override
    public List<Publication> findPublicationByAuthor(User author) {
        return publicationsRepository.findByAuthorOrderByCreatedAtDesc(author);
    }
}
