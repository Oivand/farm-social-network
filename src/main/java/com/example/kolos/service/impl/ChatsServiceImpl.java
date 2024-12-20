package com.example.kolos.service.impl;

import com.example.kolos.repository.ChatsRepository;
import com.example.kolos.service.ChatsService;
import com.example.kolos.model.Chats;
import com.example.kolos.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatsServiceImpl implements ChatsService {
    private final ChatsRepository chatsRepository;

    public ChatsServiceImpl(ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    @Override
    public List<Chats> findChatsUserAsFounder(User founderChat) {
        if (founderChat == null) {
            throw new IllegalArgumentException("Ошибка: Владелец чата (founderChat) не указан.");
        }
        return chatsRepository.findByFounderChat(founderChat);
    }

    @Override
    public List<Chats> findChatsUserAsMembers(User member) {
        if (member == null) {
            throw new IllegalArgumentException("Ошибка: Участник чата (member) не указан.");
        }
        return chatsRepository.findByMembers(member);
    }

    @Override
    public Optional<Chats> findById(Long idChat) {
        if (idChat == null) {
            throw new IllegalArgumentException("Ошибка: ID чата (idChat) не указан.");
        }
        return chatsRepository.findById(idChat);
    }

    @Override
    public List<Chats> findAll() {
        List<Chats> chats = chatsRepository.findAll();
        if (chats.isEmpty()) {
            throw new IllegalStateException("Ошибка: Список чатов пуст. Чаты не найдены.");
        }
        return chats;
    }
}
