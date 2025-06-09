package com.example.kolos.service.impl;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User;
import com.example.kolos.repository.PublicationsRepository;
import com.example.kolos.repository.UserRepository;
import com.example.kolos.service.PublicationsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationServiceImpl implements PublicationsService {

    private final PublicationsRepository publicationRepository;
    private final UserRepository userRepository;

    public PublicationServiceImpl(PublicationsRepository publicationRepository, UserRepository userRepository) {
        this.publicationRepository = publicationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Publication> find10LatestPublication() {
        return publicationRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public List<Publication> findPublicationByDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        return publicationRepository.findByDescriptionContainingOrderByCreatedAtDesc(description);
    }

    @Override
    public List<Publication> findPublicationByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        return publicationRepository.findByTitleContainingOrderByCreatedAtDesc(title);
    }

    // Метод findPublicationByAuthor(User author) УДАЛЕН из реализации, так как его нет в интерфейсе.

    @Override
    public Optional<Publication> findById(Long idPublication) {
        if (idPublication == null) {
            throw new IllegalArgumentException("Publication ID cannot be null.");
        }
        return publicationRepository.findById(idPublication);
    }

    @Override
    public List<Publication> findPublicationByAuthorId(Long authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("Author ID cannot be null.");
        }
        // Используем метод репозитория, который принимает ID автора
        return publicationRepository.findByAuthor_IdUserOrderByCreatedAtDesc(authorId);
    }

    @Override
    public List<Publication> findAllPublicationsOrderedByDate() {
        return publicationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public Publication save(Publication publication) {
        if (publication == null) {
            throw new IllegalArgumentException("Publication cannot be null.");
        }
        if (publication.getTitle() == null || publication.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Publication title cannot be empty.");
        }
        if (publication.getDescription() == null || publication.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Publication description cannot be empty.");
        }
        if (publication.getAuthor() == null || publication.getAuthor().getIdUser() == null) {
            throw new IllegalArgumentException("Publication must have an author with a valid ID.");
        }

        User existingAuthor = userRepository.findById(publication.getAuthor().getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Author with ID " + publication.getAuthor().getIdUser() + " not found."));
        publication.setAuthor(existingAuthor);

        return publicationRepository.save(publication);
    }

    @Override
    @Transactional
    public Publication update(Long idPublication, Publication updatedPublication) {
        if (idPublication == null) {
            throw new IllegalArgumentException("Publication ID for update cannot be null.");
        }
        if (updatedPublication == null) {
            throw new IllegalArgumentException("Updated publication object cannot be null.");
        }
        if (updatedPublication.getTitle() == null || updatedPublication.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Publication title cannot be empty during update.");
        }
        if (updatedPublication.getDescription() == null || updatedPublication.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Publication description cannot be empty during update.");
        }

        Publication existingPublication = publicationRepository.findById(idPublication)
                .orElseThrow(() -> new IllegalArgumentException("Publication with ID " + idPublication + " not found for update."));

        existingPublication.setTitle(updatedPublication.getTitle());
        existingPublication.setDescription(updatedPublication.getDescription());
        existingPublication.setPathMedia(updatedPublication.getPathMedia());

        if (updatedPublication.getAuthor() != null && updatedPublication.getAuthor().getIdUser() != null) {
            User newAuthor = userRepository.findById(updatedPublication.getAuthor().getIdUser())
                    .orElseThrow(() -> new IllegalArgumentException("New author with ID " + updatedPublication.getAuthor().getIdUser() + " not found."));
            existingPublication.setAuthor(newAuthor);
        } else if (updatedPublication.getAuthor() != null) {
            throw new IllegalArgumentException("Author ID cannot be null if author object is provided for update.");
        }

        return publicationRepository.save(existingPublication);
    }

    @Override
    @Transactional
    public void deleteById(Long idPublication) {
        if (idPublication == null) {
            throw new IllegalArgumentException("Publication ID for deletion cannot be null.");
        }
        if (!publicationRepository.existsById(idPublication)) {
            throw new IllegalArgumentException("Publication with ID " + idPublication + " not found for deletion.");
        }
        publicationRepository.deleteById(idPublication);
    }
}