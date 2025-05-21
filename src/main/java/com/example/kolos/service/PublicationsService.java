package com.example.kolos.service;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface PublicationsService {

    // Получить последние 10 опубликованных материалов
    List<Publication> find10LatestPublication();

    // Найти публикации по описанию
    List<Publication> findPublicationByDescription(String description);

    // Найти публикации по заголовку
    List<Publication> findPublicationByTitle(String title);

    // Найти публикации по автору
    List<Publication> findPublicationByAuthor(User author);

    // Найти публикацию по ID
    Optional<Publication> findById(Long idPublication);

    // Найти публикации по ID автора
    List<Publication> findPublicationByAuthorId(Long authorId);

    //сохранить новую публикацию
    Publication save(Publication publication);

    //удалить публикацию
    void deleteById(Long idPublication);
}
