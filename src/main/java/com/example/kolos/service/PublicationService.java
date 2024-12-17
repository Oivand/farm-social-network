package com.example.kolos.service;

import com.example.kolos.interfaces.PublicationsInterface;
import com.example.kolos.model.Publication;
import com.example.kolos.model.User;
import com.example.kolos.repository.PublicationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PublicationService /*implements PublicationsInterface */{

//    private final PublicationsRepository publicationsRepository;
//
//    @Override
//    public Optional<Publication> getById(Long idPublication) {
//        return publicationsRepository.findById(idPublication);
//    }
//
//    @Override
//    public List<Publication> getByAuthor(User author) {
//        return publicationsRepository.findByAuthorIdUser(author);
//    }
//
//    @Override
//    public List<Publication> getByTitle(String title) {
//        return publicationsRepository.findByTitleContaining(title);
//    }
//
//    @Override
//    public List<Publication> getByDescription(String description) {
//        return publicationsRepository.findByDescription(description);
//    }
//
//    @Override
//    public List<Publication> getByCreatedAt(LocalDateTime createdAt) {
//        return publicationsRepository.getByCreatedAt(createdAt);
//    }
//
//    @Override
//    public List<Publication> getAllSortedByCreatedAtDesc() {
//        return publicationsRepository.findTop10ByOrderByCreatedAtDesc();
//    }
//
//    @Override
//    public List<Publication> getAllSortedByCreatedAtAsс() {
//        return publicationsRepository.findTop10ByOrderByCreatedAtAsc();
//    }
}


