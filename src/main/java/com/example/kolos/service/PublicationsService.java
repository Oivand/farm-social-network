package com.example.kolos.service;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User;

import java.util.List;

public interface PublicationsService {
    List<Publication> find10LatestPublication();
    List<Publication> findPublicationByDescription(String description);
    List<Publication> findPublicationByTitle(String title);
    List<Publication> findPublicationByAuthor(User author);
}
