package com.example.kolos.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User;

public interface PublicationsInterface {
    List<Publication> find10LatestPublication();

    List<Publication> findPublicationByDescription(String description);

    List<Publication> findPublicationByTitle(String title);

    List<Publication> findPublicationByAuthor(User author);
}
