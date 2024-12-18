package com.example.kolos.service.impl;

import com.example.kolos.interfaces.ChatsInterface;
import com.example.kolos.model.Chats;
import com.example.kolos.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatsServiceImpl implements ChatsInterface {
    @Override
    public List<Chats> findChatsUserAsFounder(User founderChat) {
        return List.of();
    }

    @Override
    public List<Chats> findChatsUserAsMembers(User member) {
        return List.of();
    }
}
