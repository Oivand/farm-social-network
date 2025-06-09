package com.example.kolos.repository;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Добавлен импорт Optional для методов, которые могут вернуть 0 или 1 результат

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {

    // Улучшение 1: Точный поиск по имени группы.
    // Часто имя группы должно быть уникальным. Optional<Groups> лучше подходит для случаев 0 или 1 результата.
    // Добавлен IgnoreCase для большей гибкости при поиске.
    Optional<Groups> findByGroupNameIgnoreCase(String groupName);

    // Улучшение 2: Поиск по имени группы с частичным совпадением (как у вас).
    // Добавлен IgnoreCase для игнорирования регистра при поиске.
    List<Groups> findByGroupNameContainingIgnoreCaseOrderByCreatedAtDesc(String groupName);

    // Улучшение 3: Поиск всех групп, созданных конкретным мастером (по объекту User).
    // Ваш метод findByGroupMasterOrderByCreatedAtDesc(User groupMaster) был корректен.
    List<Groups> findByGroupMasterOrderByCreatedAtDesc(User groupMaster);

    // Улучшение 4: Поиск всех групп, созданных по ID мастера.
    // Это более гибкий подход для API, когда у вас есть только ID пользователя.
    // Spring Data JPA транслирует это в SQL-запрос по id_user внутри group_master.
    List<Groups> findByGroupMaster_IdUserOrderByCreatedAtDesc(Long groupMasterId);

    // Улучшение 5: Найти все группы, в которых состоит определенный пользователь.
    // Использует ManyToMany связь 'members'.
    List<Groups> findByMembersContainingOrderByCreatedAtDesc(User user);

    // Улучшение 6: Найти все группы, в которых состоит пользователь по его ID.
    List<Groups> findByMembers_IdUserOrderByCreatedAtDesc(Long userId);

    // Улучшение 7: Получить все группы, отсортированные по дате создания.
    // Общая полезная функция.
    List<Groups> findAllByOrderByCreatedAtDesc();
}