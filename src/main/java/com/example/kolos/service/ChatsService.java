package com.example.kolos.service;

import com.example.kolos.model.Chats;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface ChatsService {
    // Найти все чаты, созданные конкретным пользователем
    List<Chats> findChatsUserAsFounder(User founderChat);

    // Найти все чаты, в которых участвует конкретный пользователь
    List<Chats> findChatsUserAsMembers(User member);

    Optional<Chats> findById(Long idChat);

    List<Chats> findAll();
}
