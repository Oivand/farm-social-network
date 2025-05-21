package com.example.kolos.service;

import com.example.kolos.model.Chats;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface ChatsService {
    // Найти чаты, созданные конкретным пользователем
    List<Chats> findChatsByFounder(User founderChat);

    // Найти чаты, в которых участвует конкретный пользователь
    List<Chats> findChatsByMember(User member);

    // Найти чат по ID
    Optional<Chats> findById(Long idChat);

    // Получить все чаты
    List<Chats> findAll();

    // Сохранить новый чат
    Chats save(Chats chats);

    // Добавить пользователя в чат
    void addUserToChat(Long chatId, User user);

    // Удалить пользователя из чата
    void removeUserFromChat(Long chatId, User user);
}
