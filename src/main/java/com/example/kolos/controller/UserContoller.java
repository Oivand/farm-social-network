package com.example.kolos.controller;

import com.example.kolos.model.User;
import com.example.kolos.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserContoller {
    private final UserService userService;

    public UserContoller(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getallUsers();
//    }
}
