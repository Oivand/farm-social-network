package com.example.kolos.service;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User; // Этот импорт все еще нужен, так как Publication ссылается на User

import java.util.List;
import java.util.Optional;

public interface PublicationsService {

    // Получить последние 10 опубликованных материалов
    List<Publication> find10LatestPublication();

    // Найти публикации по описанию (частичное совпадение)
    List<Publication> findPublicationByDescription(String description);

    // Найти публикации по заголовку (частичное совпадение)
    List<Publication> findPublicationByTitle(String title);

    // Найти публикации по ID автора
    List<Publication> findPublicationByAuthorId(Long authorId);

    // Найти публикацию по ID
    Optional<Publication> findById(Long idPublication);

    // Получить все публикации (отсортированные по дате создания)
    List<Publication> findAllPublicationsOrderedByDate();

    // --- CRUD Operations ---
    Publication save(Publication publication);
    Publication update(Long idPublication, Publication updatedPublication);
    void deleteById(Long idPublication);
}