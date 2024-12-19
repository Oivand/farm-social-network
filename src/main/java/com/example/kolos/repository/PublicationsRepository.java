package com.example.kolos.repository;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationsRepository extends JpaRepository<Publication, Long> {
    // Получить все публикации конкретного пользователя
    List<Publication> findByAuthorOrderByCreatedAtDesc(User author);

    // Найти публикацию по заголовку (для поиска)
    List<Publication> findByTitleContainingOrderByCreatedAtDesc(String title);

    // Получить последние опубликованные публикации
    List<Publication> findTop10ByOrderByCreatedAtDesc();

    // Найти по слову из описания
    List<Publication> findByDescriptionContainingOrderByCreatedAtDesc(String description);
}
