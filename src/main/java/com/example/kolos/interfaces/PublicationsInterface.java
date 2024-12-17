package com.example.kolos.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User;

public interface PublicationsInterface {
    // Поиск по ID возвращает Optional
    Optional<Publication> getById(Long idPublication);
    // Автор может быть связан с несколькими публикациями
    List<Publication> getByAuthor(User author);
    // Название может быть неуникальным
    List<Publication> getByTitle(String title);
    // Описание также может быть неуникальным
    List<Publication> getByDescription(String description);
    // Публикации на определённую дату
    List<Publication> getByCreatedAt(LocalDateTime createdAt);
    // Сортировка всех публикаций по дате (убывающий порядок)
    List<Publication> getAllSortedByCreatedAtDesc();
    //Сортировка всех публикаций по дате (возрастающий порядок)
    List<Publication> getAllSortedByCreatedAtAsс();
}
