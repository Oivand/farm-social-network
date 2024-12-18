package com.example.kolos.interfaces;

import com.example.kolos.model.Chats;
import com.example.kolos.model.User;

import java.util.List;

public interface ChatsInterface {
    // Найти все чаты, созданные конкретным пользователем
    List<Chats> findChatsUserAsFounder(User founderChat);

    // Найти все чаты, в которых участвует конкретный пользователь
    List<Chats> findChatsUserAsMembers(User member);
}
