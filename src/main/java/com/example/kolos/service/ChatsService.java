package com.example.kolos.service;

import com.example.kolos.model.Chats;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface ChatsService {

    // --- CRUD Operations ---

    /**
     * Создает новый чат.
     * Если Chats.idChat == null, создается новый.
     * @param chat Объект чата для сохранения.
     * @return Сохраненный объект чата.
     */
    Chats save(Chats chat);

    /**
     * Обновляет существующий чат по его ID.
     * Обратите внимание: для обновления полей чата, кроме участников, используйте этот метод.
     * Для управления участниками используйте addUserToChat/removeUserFromChat.
     * @param chatId ID чата, который нужно обновить.
     * @param updatedChat Объект с новыми данными для чата.
     * @return Обновленный объект чата.
     */
    Chats update(Long chatId, Chats updatedChat); // Улучшение: добавлен метод update

    /**
     * Удаляет чат по его ID.
     * @param chatId ID чата, который нужно удалить.
     */
    void deleteById(Long chatId); // Улучшение: добавлен метод deleteById


    // --- Search Operations ---

    /**
     * Находит чаты, созданные конкретным пользователем (по объекту User).
     * @param founderChat Объект пользователя-основателя.
     * @return Список чатов, созданных этим пользователем.
     */
    List<Chats> findChatsByFounder(User founderChat);

    /**
     * Находит чаты, созданные конкретным пользователем (по ID пользователя).
     * Это более универсальный метод для использования в контроллере.
     * @param founderChatId ID пользователя-основателя.
     * @return Список чатов, созданных этим пользователем.
     */
    List<Chats> findChatsByFounderId(Long founderChatId); // Улучшение: добавлен поиск по ID основателя

    /**
     * Находит чаты, в которых участвует конкретный пользователь (по объекту User).
     * @param member Объект пользователя-участника.
     * @return Список чатов, в которых участвует этот пользователь.
     */
    List<Chats> findChatsByMember(User member);

    /**
     * Находит чаты, в которых участвует конкретный пользователь (по ID пользователя).
     * Это более универсальный метод для использования в контроллере.
     * @param userId ID пользователя-участника.
     * @return Список чатов, в которых участвует этот пользователь.
     */
    List<Chats> findChatsByMemberId(Long userId); // Улучшение: добавлен поиск по ID участника

    /**
     * Находит чат по ID.
     * @param idChat ID чата.
     * @return Optional с найденным чатом, если он существует.
     */
    Optional<Chats> findById(Long idChat);

    /**
     * Получает все чаты, отсортированные по дате создания.
     * @return Список всех чатов.
     */
    List<Chats> findAllChatsOrderedByDate(); // Улучшение: добавлен метод для получения всех чатов с сортировкой

    /**
     * Получает все чаты (без специфической сортировки, как в JpaRepository).
     * @return Список всех чатов.
     */
    List<Chats> findAll();


    // --- Methods for Managing Members (Unaltered, but noted) ---

    /**
     * Добавляет пользователя в чат.
     * @param chatId ID чата.
     * @param userId ID пользователя для добавления (Улучшение: параметр User заменен на Long userId).
     * @return Обновленный объект чата.
     */
    Chats addUserToChat(Long chatId, Long userId); // Улучшение: возвращает Chats и принимает Long userId

    /**
     * Удаляет пользователя из чата.
     * @param chatId ID чата.
     * @param userId ID пользователя для удаления (Улучшение: параметр User заменен на Long userId).
     * @return Обновленный объект чата.
     */
    Chats removeUserFromChat(Long chatId, Long userId); // Улучшение: возвращает Chats и принимает Long userId
}