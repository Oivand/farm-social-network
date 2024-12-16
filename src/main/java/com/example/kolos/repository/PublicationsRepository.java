package com.example.kolos.repository;

import com.example.kolos.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationsRepository extends JpaRepository<Publication, Long> {
    // Получить все публикации конкретного пользователя
    List<Publication> findByAuthorIdUser(Long authorId);

    // Найти публикацию по заголовку (для поиска)
    List<Publication> findByTitleContaining(String title);

    // Получить публикацию по ID
    Optional<Publication> findPublicationByidPublication(Long idPublication);

    // Получить последние опубликованные публикации
    List<Publication> findTop10ByOrderByCreatedAtDesc();
}
