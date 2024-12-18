package com.example.kolos.service.impl;

import com.example.kolos.interfaces.PublicationsInterface;
import com.example.kolos.model.Publication;
import com.example.kolos.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationServiceImpl implements PublicationsInterface {
    @Override
    public List<Publication> find10LatestPublication() {
        return List.of();
    }

    @Override
    public List<Publication> findPublicationByDescription(String description) {
        return List.of();
    }

    @Override
    public List<Publication> findPublicationByTitle(String title) {
        return List.of();
    }

    @Override
    public List<Publication> findPublicationByAuthor(User author) {
        return List.of();
    }
}
