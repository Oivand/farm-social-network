package com.example.kolos.controller;

import com.example.kolos.model.Chats;
import com.example.kolos.service.ChatsService;
import org.springframework.stereotype.Controller;

@Controller
public class ChatsContoller {
    private final ChatsService chatsService;

    public ChatsContoller(ChatsService chatsService) {
        this.chatsService = chatsService;
    }
}
