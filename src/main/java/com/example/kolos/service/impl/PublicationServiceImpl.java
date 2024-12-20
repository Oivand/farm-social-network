package com.example.kolos.service.impl;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User;
import com.example.kolos.repository.PublicationsRepository;
import com.example.kolos.service.PublicationsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        // Проверка на null или пустое описание
        if (description == null || description.isEmpty()){
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        return publicationsRepository.findByDescriptionContainingOrderByCreatedAtDesc(description);
    }

    @Override
    public List<Publication> findPublicationByTitle(String title) {
        // Проверка на null или пустой заголовок
        if (title == null || title.isEmpty()){
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        return publicationsRepository.findByTitleContainingOrderByCreatedAtDesc(title);
    }

    @Override
    public List<Publication> findPublicationByAuthor(User author) {
        // Проверка на null для автора
        if (author == null){
            throw new IllegalArgumentException("Author cannot be null.");
        }
        return publicationsRepository.findByAuthorOrderByCreatedAtDesc(author);
    }


    @Override
    public Optional<Publication> findById(Long idPublication) {
        if (idPublication == null) {
            throw new IllegalArgumentException("Ошибка: ID публикации (id) не может быть null.");
        }
        return publicationsRepository.findById(idPublication);
    }
}
