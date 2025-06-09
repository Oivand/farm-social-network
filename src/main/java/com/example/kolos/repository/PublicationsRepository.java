package com.example.kolos.repository;

import com.example.kolos.model.Publication;
import com.example.kolos.model.User; // This import is correct, but not directly used in method signatures here
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Added Optional for findById if it were here explicitly

@Repository
public interface PublicationsRepository extends JpaRepository<Publication, Long> {
    // Получить все публикации конкретного пользователя по объекту User
    List<Publication> findByAuthorOrderByCreatedAtDesc(User author); // Changed parameter from Long to User

    // Получить все публикации конкретного пользователя по ID автора
    List<Publication> findByAuthor_IdUserOrderByCreatedAtDesc(Long authorId);// Added this for convenience

    // Найти публикацию по заголовку (для поиска, частичное совпадение)
    List<Publication> findByTitleContainingOrderByCreatedAtDesc(String title);

    // Получить последние опубликованные публикации (например, 10)
    List<Publication> findTop10ByOrderByCreatedAtDesc();

    // Найти по слову из описания (частичное совпадение)
    List<Publication> findByDescriptionContainingOrderByCreatedAtDesc(String description);

    // Добавил метод для получения всех публикаций (наследуется от JpaRepository, но явно для наглядности)
    List<Publication> findAllByOrderByCreatedAtDesc();
}