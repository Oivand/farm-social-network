package com.example.kolos.controller;

import com.example.kolos.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserContoller {
    private final UserService userService;

    public UserContoller(UserService userService) {
        this.userService = userService;
    }
}
