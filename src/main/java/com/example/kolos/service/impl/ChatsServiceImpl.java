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
        return chatsRepository.findByFounderChat(founderChat);
    }

    @Override
    public List<Chats> findChatsUserAsMembers(User member) {
        return chatsRepository.findByMembers(member);
    }

    @Override
    public Optional<Chats> findById(Long idChat) {
        return chatsRepository.findById(idChat);
    }

    @Override
    public List<Chats> findAll() {
        return chatsRepository.findAll();
    }
}
