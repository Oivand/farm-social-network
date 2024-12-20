package com.example.kolos.controller;

import com.example.kolos.model.Chats;
import com.example.kolos.service.ChatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/chats")
public class ChatsContoller {
    private final ChatsService chatsService;

    public ChatsContoller(ChatsService chatsService){
        this.chatsService = chatsService;
    }

}
